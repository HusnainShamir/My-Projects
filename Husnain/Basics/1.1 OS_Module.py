import os #imports the OS Module

directory_path = "Z:\\Coding\\Python\\Basics" #The paths

contents = os.listdir(directory_path) #determine all the content

print(f"Contents of '{directory_path}':") #prints the content line
for item in contents: #prints one by one
    print(item) # print the content