with open('test.txt', 'r') as f:
    lines = f.readlines()
    lines = [s.replace("\n"," ") for s in lines]
    print(lines)