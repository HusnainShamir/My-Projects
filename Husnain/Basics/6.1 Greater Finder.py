a = int(input("Enter Numbers : "))
b = int(input("Enter Numbers : "))
c = int(input("Enter Numbers : "))
d = int(input("Enter Numbers : "))
if(a>b and a>c and a>d ):
    print(f"{a} is Greatest of All")
elif(b>a and b>c and b>d ):
    print(f"{b} is Greatest of All")
elif(c>a and c>b and c>d ):
    print(f"{c} is Greatest of All")
else:
    print(f"{d} is Greatest of All")