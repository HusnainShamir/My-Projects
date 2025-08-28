english = {
    "book" : "Kitab"
    ,"pencil" : "Qelem"
    ,"computer" : "Kompüter"
    ,"keyboard" : "Klaviatura"
    ,"mouse" : "Siçan"
}

a= input("Enter the word in English: ")
print(english.get(a, "This word is not found in the dictionary."))