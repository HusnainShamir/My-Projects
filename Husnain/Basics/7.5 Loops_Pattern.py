#    Write a program to print the following star pattern.
#     *
#    ***
#   ***** for n = 3
n = int(input("Enter a Number : "))
'''
for i in range(1, n+1):
    print(" "*(n-i),end='')
    print("*"*(2*i-1),end='')
    print("") 
'''

#    Write a program to print the following star pattern.
#   *
#   **
#   *** for n = 3
'''
for j in range(1,n+1):
    print("*"*(j),end="")
    print("")
'''
for k in range(1,n+1):
    for l in range(1,n+1):
        if(k!=l):
            print("*",end="")
    print("")