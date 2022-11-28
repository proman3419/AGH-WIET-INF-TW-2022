import re
from itertools import product
from collections import defaultdict
import graphviz
import os
import pydot


class SchedulingProblem:
    def __init__(self, productions_raw, alphabet_raw, word_raw):
        self.productions = self.parse_productions(productions_raw)
        self.alphabet = self.parse_alphabet(alphabet_raw)
        self.word = self.parse_word(word_raw)
        self.N = len(self.word)
        self.D = None # dependency relation
        self.D_min_matrix = None # helper variable for creating the dependencies graph
        self.I = None # independency relation
        self.FNF = None # Foata's normal form
        self.dependencies_graph = None

    # parsing #################################################################
    def parse_productions(self, productions_raw):
        productions = {}
        for production_raw in productions_raw.split('\n')[:-1]:
            production_raw = production_raw.split(')')
            label = production_raw[0][1:]
            production_raw = production_raw[1][1:].split(":=")
            l = production_raw[0][:-1]
            r = set(re.findall(r"[A-Za-z]+", production_raw[1]))
            productions[label] = (l, r)
        return productions

    def parse_alphabet(self, alphabet_raw):
        return set(alphabet_raw.split('{')[1][:-1].split(", "))

    def parse_word(self, word_raw):
        return list(word_raw.split('=')[1][1:])
    # parsing #################################################################

    def solve(self):
        self.calculate_dependency_relation()
        self.calculate_independency_relation()
        self.calculate_foata_normal_form()
        self.generate_dependencies_graph()

    # two productions are dependant if any of two wants to write to a variable shared between them
    def check_if_dependent(self, i, j):
        def read_write_check(x, y): return self.productions[self.word[x]][0] in self.productions[self.word[y]][1]
        def write_write_check(x, y): return self.productions[self.word[x]][0] == self.productions[self.word[y]][0]
        return read_write_check(i, j) or read_write_check(j, i) or write_write_check(i, j)

    def calculate_dependency_relation(self):
        D = set(map(lambda x: (x, x), self.word)) # start with (x, x) for all characters in the word as they're always in D
        D_min_matrix = [[0 for _ in range(self.N)] for _ in range(self.N)] # ignore (x, x) as they're redundant in the graph
        for i in range(self.N):
            for j in range(i + 1, self.N): # there's no need to look back (symetry)
                if self.check_if_dependent(i, j):
                    D_min_matrix[i][j] = 1 # add one way relation, the graph is directed
                    D.add((self.word[i], self.word[j])) # add a symmetrical relation
                    D.add((self.word[j], self.word[i]))
        self.D = D
        self.D_min_matrix = D_min_matrix

    # the independency relation can be described as A^2 - D, where A is the alphabet
    def calculate_independency_relation(self):
        self.I = set(product(self.word, repeat=2)) - self.D

    # implementation of the algorithm attatched in the task's description (the link doesn't work anymore but
    # I found the document: https://citeseerx.ist.psu.edu/pdf/d67ac4c1e5967f7e114f390245f28909f259c034)
    # the algorithm:
    # 1. for each character in the alphabet create a stack
    # 2. scan the word from right to left, when processing a character ch:
    #   a) push ch on its stack
    #   b) for all characters that are in a dependency relation with ch push a marker (*) on theirs stacks
    # 3. loop next steps until all stacks are empty
    # 4. pop characters from all stacks, they create a Foata's class, ignore markers
    # 5. for all characters that are in a dependency relation with ch remove a marker from theirs stacks
    def calculate_foata_normal_form(self):
        empty_stacks_cnt = self.N
        stacks = defaultdict(list) # 1
        for ch in reversed(self.word): # 2
            if len(stacks[ch]) == 0:
                empty_stacks_cnt -= 1
            stacks[ch].append(ch) # a)
            for ach in self.alphabet: # b)
                if ach != ch and (ch, ach) in self.D:
                    stacks[ach].append('*')

        FNF = []
        while empty_stacks_cnt < self.N: # 3
            tops = [s[-1] for s in filter(lambda x: len(x) > 0 and x[-1] != '*', stacks.values())] # 4
            for ch in tops:
                stacks[ch].pop()
                empty_stacks_cnt += int(len(stacks[ch]) == 0)
                for ach in self.alphabet: # 5
                    if ach != ch and (ch, ach) in self.D:
                        stacks[ach].pop()
                        empty_stacks_cnt += int(len(stacks[ach]) == 0)
            FNF.append(sorted(tops))
        self.FNF = FNF

    # if there are edges i -> j, j -> k then remove i -> k (if exists)
    def remove_transitive_edges(self):
        for i in range(self.N):
            for j in range(self.N):
                if self.D_min_matrix[i][j]:
                    for k in range(self.N):
                        if self.D_min_matrix[j][k]:
                            self.D_min_matrix[i][k] = 0

    # the graph was created along with the dependency relation
    # now it needs to be simplified and converted to the graphviz format
    def generate_dependencies_graph(self):
        self.remove_transitive_edges()
        dot = graphviz.Digraph("dependencies_graph")
        for i, ch in enumerate(self.word):
            dot.node(name=str(i), label=ch)
        for i in range(len(self.word)):
            for j in range(len(self.word)):
                if self.D_min_matrix[i][j]:
                    dot.edge(str(i), str(j))
        self.dependencies_graph = dot

    def save(self, save_dir):
        try:
            self.dependencies_graph.render(directory=save_dir)
            solution_save_path = os.path.join(save_dir, "solution.txt")
            with open(solution_save_path, "w+") as f:
                f.write(str(self))
            graph_dot_file = os.path.join(save_dir, "dependencies_graph.gv")
            (graph,) = pydot.graph_from_dot_file(graph_dot_file)
            graph_png_save_path = os.path.join(save_dir, "dependencies_graph.png")
            graph.write_png(graph_png_save_path)
        except AttributeError:
            print("Run `solve` first")

    def _relation_pretty_str(self, relation):
        s = "{"
        for r in relation:
            s += f"({r[0]}, {r[1]}), "
        s = s[:-2] + '}'
        return s

    def _foata_normal_form_pretty_str(self):
        s = ''
        for fc in self.FNF:
            s += f"({''.join(fc)})"
        return s

    def __str__(self):
        try:
            pD = self._relation_pretty_str(sorted(self.D))
            pI = self._relation_pretty_str(sorted(self.I))
            pFNF = self._foata_normal_form_pretty_str()
            return f"D = {pD}\nI = {pI}\nFNF = {pFNF}\n"
        except (TypeError, AttributeError):
            return "Run `solve` first"
