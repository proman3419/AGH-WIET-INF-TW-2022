from SchedulingProblem import SchedulingProblem


problem1 = SchedulingProblem("""(a) x := x + y
(b) y := y + 2z
(c) x := 3x + z
(d) z := y − z
""",
"A = {a, b, c, d}",
"w = baadcb")
problem1.solve()
problem1.save("solution1")
print("=== PROBLEM 1 SOLUTION ===")
print(problem1)

problem2 = SchedulingProblem("""(a) x := x + 1
(b) y := y + 2z
(c) x := 3x + z
(d) w := w + v
(e) z := y − z
(f) v := x + v
""",
"A = {a, b, c, d, e, f}",
"w = acdcfbbe")
problem2.solve()
problem2.save("solution2")
print("=== PROBLEM 2 SOLUTION ===")
print(problem2)
