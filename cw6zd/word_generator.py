def generate_word(N):
    for i in range(N):
        for k in range(i + 1, N):
            print("A_{" + f"{k},{i}" + "}", end=", ")
            for j in range(i, N + 1):
                print("B_{" + f"{k},{j},{i}" + "}", end=", ")
                print("C_{" + f"{k},{j},{i}" + "}", end=", ")


generate_word(3)
