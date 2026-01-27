import json
import os,time,random
from datetime import datetime as dt

t = 0.8
# Helpers
def clear():
    os.system("cls" if os.name=='nt' else 'clear')
def load(file):
    with open(f"data/{file}.json","r") as f:
        data=json.load(f)
    return data
def dump(file,data):
    with open(f"data/{file}.json","w") as f:
        json.dump(data,f,indent=4)

# Login
def login_menu():
    print("=" * 30, "\t  LOGIN MENU", "=" * 30, sep="\n")
    print()
    u = input("Username : ").lower()
    p = input("Password : ")

    data=load("users")
    if u in data.keys() and p == data[u][1]:
        return data[u]
    else:
        return None
def id_gen():
    used_ids=set()
    while True:
        num = random.randint(1000, 9999)
        if num not in used_ids:
            used_ids.add(num)
            return num
def login_history(u,s,_id=0):
    if _id == 0:
        _id = id_gen()
    log = {
        "Login ID": _id ,
        "Username": u[0],
        "Password": u[1],
        "Status": s,
        "Timestamp": dt.now().strftime("%Y-%m-%d %H:%M:%S")
    }
    data = load("loginhistory")
    data["login_logs"].append(log)
    dump("loginhistory",data)

    return _id
def logout(u,id):
    data= load("loginhistory")
    for log in data["login_logs"]:
        if log["Username"] == u and log["Status"]=="Logged In" and log["Login ID"] == id :
            login_history(u,"Logged Out",log["Login ID"])

# AUDITOR'S TOOL
def login_h(id__):
    print("=" * 30, "\tLOGIN HISTORY", "=" * 30, sep="\n")
    print()
    data= load("loginhistory")
    for logs in data["login_logs"]:
        if logs["Status"]=="Logged In":
            log_out=log(data,logs["Login ID"])
            print(history_formatter(logs,log_out))
def log(data,l_id):
    for x in data["login_logs"]:
        if x["Login ID"]==l_id and x["Status"]=="Logged Out":
            return x
    return { "Status": "No Logout Data","Timestamp": "00" }
def search_msg(u):
    print("=" * 30, "\t ALL MESSAGES", "=" * 30, sep="\n")
    print()
    data=load("messages")
    print(f":: Private Messages of {u[0].capitalize()} ::")
    for msg in data["Messages"]:
        if msg["Type"]== "Private":
            if msg["Sender"]==u[0]:
                print(a_msg_formatter(msg))
            if msg["Receiver"]==u[0]:
                print(a_msg_formatter(msg))
    print()
    print(f":: Public Messages of {u[0].capitalize()} ::")
    for msg in data["Messages"]:
        if msg["Type"] == "Public":
            if msg["Sender"]==u[0]:
                print(a_msg_formatter(msg))
            if msg["Receiver"]==u[0]:
                print(a_msg_formatter(msg))
    return
def search_mail(u):
    print("=" * 30, "\t  VIEW MAIL", "=" * 30, sep="\n")
    data = load("emails")
    print(f": Mails Recieved by {u[0]} :")
    print()
    for mail in data["Mails"]:
        if mail["Receiver"] == u[2]:
            print(mail_formatter(mail))
    print(f": Mails Sent by {u[0]} :")
    print()
    for mail in data["Mails"]:
        if mail["Sender"] == u[2]:
            print(mail_formatter(mail))
def search_msg_dt():
    try:
        s = input("Start (YYYY-MM-DD): ")
        e = input("End   (YYYY-MM-DD): ")

        s_t = dt.strptime(s, "%Y-%m-%d")
        e_t = dt.strptime(e, "%Y-%m-%d")

    except:
        print("Invalid format!")
        time.sleep(0.6)
        return

    msgs = load("messages")["Messages"]

    found = False
    print()
    for m in msgs:
        ts = dt.strptime(m["Timestamp"], "%Y-%m-%d %H:%M:%S")
        if s_t <= ts <= e_t:
            print(a_msg_formatter(m))
            found = True

    if not found:
        print("No messages in this range.")
def count_msg(u):
    print("=" * 30, "\t ALL MESSAGES", "=" * 30, sep="\n")
    print()

    data= load("messages")["Messages"]
    pr_r=[x for x in data if x["Type"]=="Private" and x["Receiver"]== u[0]]
    pr_s=[x for x in data if x["Type"]=="Private" and x["Sender"]==u[0]]
    pb_r=[x for x in data if x["Type"]=="Public"  and x["Receiver"]== u[0]]
    pb_s=[x for x in data if x["Type"]=="Public"  and x["Sender"]==u[0]]


    print(f'''
Total Message of {u[0].capitalize()}: {len(pb_s)+len(pb_r)+len(pr_s)+len(pr_r)}
    Public Messages : {len(pb_r) + len(pb_s)}
        Sent    :{len(pb_s)}
        Receive :{len(pb_r)}
    Private Messages  : {len(pr_r) + len(pr_s)}
        Sent    :{len(pr_s)}
        Receive :{len(pr_r)}''')

# STUDENT TOOLS
def view_inbox(u):
    un_r_mails = [x for x in load("emails")["Mails"] if x["Receiver"] == u[2] and x["Status"] == "Unread"]
    ch= input(f"Choose the Box!\n1) Receive ({len(un_r_mails)})\n2) Sent ")
    if ch=="1":
        clear()

        print("=" * 30, "\tRECEIVED MAILS", "=" * 30, sep="\n")
        print()

        data = load("emails")
        un_r_mails = [x for x in data["Mails"] if x["Receiver"] == u[2] and x["Status"] == "Unread"]
        if len(un_r_mails) !=0:
            print(":: UNREAD ::")
            for x in data["Mails"]:
                x["Status"]="Read"
                print(mail_formatter(x))
        print()

        r_r_mails = [x for x in load("emails")["Mails"] if x["Receiver"] == u[2] and x["Status"] != "Unread"]
        if len(r_r_mails) !=0:
            print(":: READ ::")
            for x in r_r_mails:
                print(mail_formatter(x))
        dump("emails",data)

        if len(un_r_mails+r_r_mails) == 0:
            print("NO Mail to Show ")

    elif ch=="2":
        clear()

        print("=" * 30, "\t SENT MAILS", "=" * 30, sep="\n")
        print()

        s_mails = [x for x in load("emails")["Mails"] if x["Sender"] == u[2]]
        if s_mails !=0:
            for x in s_mails:
                print(mail_formatter(x))
        else:
            print("No Mail Sent!")

    else:
        print("INVALID CHOICE")

    if input():
        clear()
        return
def send_email(u):
    r = input("Recipient: ").lower()
    if r == u[2]:
        print("Can't Send Mail to Yourself!")
        time.sleep(t+0.2)
        return
    elif r in [load("users")[x][2] for x in load("users")]:
        subject = input("Subject (max 20) : ")
        if len(subject) <= 20:
            body= input("Body (max 50) : ")
            if len(body) <= 50:
                email_sender(u[2],r,subject,body)
            else:
                print("Max Length of Subject Exceeded!")
        else:
            print("Max Length of Subject Exceeded!")
    else:
        print("User not Found!")

    if input():
        clear()
        return
def view_msg(u,un_pb,un_pr):
    ch=input(f"1) Public ({len(un_pb)})\n2) Private ({len(un_pr)})\nChoose the Chat!\t")
    msg= load("messages")

    pr_m=[x for x in msg["Messages"] if x["Type"]=="Private" and (x["Receiver"]== u[0] or x["Sender"]==u[0])]

    if ch == "1":
        clear()
        print("=" * 30, "\tPUBLIC MESSAGE", "=" * 30, sep="\n")

        changed = False
        for x in msg["Messages"]:
            if x["Type"]=="Public":
                if x["Status"] == "Unread":
                    x["Status"] = "Read"
                changed = True
                print(msg_formatter(x))
        if changed:
            dump("messages", msg)

    elif ch == "2":
            users = set()
            for m in pr_m:
                if m["Sender"] != u[0]:
                    users.add(m["Sender"])
                if m["Receiver"] != u[0]:
                    users.add(m["Receiver"])

            users = list(users)

            if not users:
                print("No Private Chats!")
                return

            print("\nChoose the Chat:\n")
            for i, name in enumerate(users, 1):
                print(f"{i}) {name.capitalize()}")

            ch = input("> ")

            try:
                selected = users[int(ch) - 1]
                clear()
                print("=" * 50, f"\t {selected.upper()}'s CHAT", "=" * 50, sep="\n")

                changed = False
                for sms in msg["Messages"]:
                    if sms["Type"] == "Private":
                        if (sms["Sender"] == u[0] and sms["Receiver"] == selected) or (sms["Sender"] == selected and sms["Receiver"] == u[0]):
                            if sms["Receiver"] == u[0] and sms["Status"] == "Unread":
                                sms["Status"] = "Read"
                                changed = True

                            display_sms = sms.copy()
                            if display_sms["Sender"] == u[0]:
                                display_sms["Sender"] = "You"

                            print(msg_formatter(display_sms))
                if changed:
                    dump("messages", msg)

            except:
                print("Invalid Input!")

    if input():
        clear()
        return

# FORMATTERS
def mail_formatter(m):
    mail = f'''To: {m["Receiver"]}
From: {m["Sender"]}
Subject: {m["Subject"]}
Body: {m["Body"]}
[{m["Timestamp"]}]
---------------------------------------------'''
    return mail
def msg_formatter(m):
    msg = f'''{m["Sender"].capitalize():<10} : {m["Message"]:<30}\t[{m["Timestamp"]}]'''
    return msg
def a_msg_formatter(m):
    loc= f"{m["Sender"].capitalize()} to {m["Receiver"].capitalize()}"
    msg = f'''{loc:<20} : {m["Message"]:<30}\t[{m["Timestamp"]}]'''
    return msg
def usr_formatter(_,u):
    data = f'''Username: {u}
Role: {_[u][1]}
Department: {_[u][2]}
Email: {_[u][3]}
------------------------------------------'''
    return data
def history_formatter(li,lo):
    def timeperiod(li,lo):
        if lo["Timestamp"] == "00":
            return "Unknown"
        else:
            return abs(dt.strptime(li["Timestamp"],"%Y-%m-%d %H:%M:%S") - (dt.strptime(lo["Timestamp"],"%Y-%m-%d %H:%M:%S")))
    data=f'''
Login ID: {li["Login ID"]}
User: {li["Username"]}
Status :{li["Status"]} at {li["Timestamp"]}
        {lo["Status"]}  at {lo["Timestamp"]}
Time duration: {timeperiod(li,lo)}
----------------------------------'''
    return data

# UDF's
def m_sender(typ,u):
    sndr= u[0]
    sms,rcvr= "",""

    if typ == "Public":
        rcvr="All"
        sms= input("Message (max 30) : ")
    elif typ == "Private":
        rcvr = input("Recipient: ").lower()
        if rcvr == u[0]:
            print("Can't Send Message to Yourself!")
            time.sleep(t+0.5)
            return
        if rcvr in load("users").keys():
            sms = input("Message (max 30) : ")
        else:
            print("User not Found!")
            time.sleep(t + 0.5)
            return
    if len(sms) <= 30:
        msg ={
            "Type": typ,
            "Sender": sndr,
            "Receiver": rcvr,
            "Message": sms,
            "Timestamp": dt.now().strftime("%Y-%m-%d %H:%M:%S"),
            "Status": "Unread"
        }
        data=load("messages")
        data["Messages"].append(msg)
        dump("messages",data)
    print("Message Sent!")
    time.sleep(t + 0.5)
    clear()
def email_sender(sn,r,s,b):
    mail={
        "Type": "Mail",
        "Sender": sn,
        "Receiver": r,
        "Subject":s,
        "Body": b,
        "Timestamp": dt.now().strftime("%Y-%m-%d %H:%M:%S"),
        "Status": "Unread"
    }
    data=load("emails")
    data["Mails"].append(mail)
    dump("emails",data)
    print("Mail Sent")
    return
def info(u,_id):
    print(f'''{"."*30}
    Login ID : {_id}
    Username : {u[0]}
    Password : {u[1]}
    Mail ID  : {u[2]}
    Role     : {u[3]}
{"."*30}''')
    input()
    clear()
    return

# MENU'S
def audit_menu(user, id__):
    while True:
        clear()
        print("=" * 30, "\t AUDIT MENU", "=" * 30, sep="\n")
        print("""
1) View Private Messages
2) View Public Messages
3) View All Users
4) View All Emails
5) Search Messages by Username
6) Search Emails by Username
7) Search Messages by Date
8) Count Messages of a User
9) View Login History

10) Refresh
11) User Info
12) Logout
""")
        ch = input()
        if ch == "1":
            clear()
            print("=" * 30, "\tPRIVATE MESSAGE", "=" * 30, sep="\n")
            print()

            [print(a_msg_formatter(x)) for x in load("messages")["Messages"] if x["Type"]=="Private"]
            if input():
                clear()
        elif ch == "2":
            clear()
            print("=" * 30,"\tPUBLIC MESSAGE"   ,"=" * 30, sep="\n")
            print()

            [print(msg_formatter(x)) for x in load("messages")["Messages"] if x["Type"] == "Public"]
            if input():
                clear()
        elif ch == "3":
            clear()
            usr= lambda u: print(f'''
    Username   : {u[0]}
    Email      : {u[2]}
    Role       : {u[3]}
    Department : {u[4]}
...........................
''')
            [usr(x) for x in load("users").values()]
            if input():
                clear()
        elif ch == "4":
            clear()
            [print(mail_formatter(x)) for x in load("emails")["Mails"]]
            if input():
                clear()
        elif ch == "5":
            clear()
            u = input("Give Username to Search Messages : ")
            if u in load("users").keys():
                search_msg(load("users")[u])
            else:
                print("User not exists!")

            if input():
                clear()
        elif ch == "6":
            u = input("Give Username to Search Mails : ")
            if u in load("users").keys():
                clear()
                search_mail(load("users")[u])
            else:
                print("User not exists!")

            if input():
                clear()
        elif ch == "7":
            clear()
            print("=" * 30, "\t SEARCH BY DATE", "=" * 30, sep="\n")
            print()
            search_msg_dt()
            if input():
                clear()
        elif ch == "8":
            u = input("Give Username to Search Messages : ")
            if u in load("users").keys():
                clear()
                count_msg(load("users")[u])
            else:
                print("User not exists!")

            if input():
                clear()
        elif ch == "9":
            clear()
            login_h(id__)

            if input():
                clear()
        elif ch == "10":
            continue
        elif ch == "11":
            clear()
            print("=" * 30, "\t     INFO", "=" * 30, sep="\n")
            info(user, id__)

            if input():
                clear()
        elif ch == "12":
            login_history(user, "Logged Out", id__)
            clear()
            return
        else:
            print("INVALID CHOICE!")
def student_menu(user, id__):
    while True:
        clear()
        print("=" * 30, "\t STUDENT MENU", "=" * 30,sep="\n")

        un_pb = [x for x in load("messages")["Messages"] if x["Type"] == "Public" and x["Status"] == "Unread" and x["Receiver"] == "All" and x["Sender"]!= user[0]]
        un_pr = [x for x in load("messages")["Messages"] if x["Type"] == "Private" and x["Status"] == "Unread" and x["Receiver"]== user[0]]
        un_ml = [x for x in load("emails")["Mails"] if x["Receiver"] == user[2] and x["Status"] == "Unread"]

        print(f"""
1) Send Private Message
2) Send Public Message
3) View Messages ({len(un_pb) + len(un_pr)} Unread's)
4) Send Email
5) View Inbox ({len(un_ml)} Unread's)

6) Refresh
7) User Info
8) Logout
""")
        ch=input("> ")
        if   ch == "1":
            clear()
            print("=" * 30, "\tPRIVATE MESSAGE", "=" * 30, sep="\n")
            print()
            m_sender("Private", user)
        elif ch == "2":
            clear()
            print("=" * 30, "\tPUBLIC MESSAGE", "=" * 30, sep="\n")
            print()
            m_sender("Public", user)
        elif ch == "3":
            clear()
            print("=" * 30, "\tVIEW MESSAGES", "=" * 30, sep="\n")
            print()
            view_msg(user,un_pb,un_pr)
        elif ch == "4":
            clear()
            print("=" * 30, "\t  SEND MAIL", "=" * 30, sep="\n")
            print()
            send_email(user)
        elif ch == "5":
            clear()
            print("=" * 30, "\t   INBOX", "=" * 30, sep="\n")
            view_inbox(user)
        elif ch == "6":
            continue
        elif ch == "7":
            clear()
            print("=" * 30,"\t     INFO"        ,"=" * 30, sep="\n")
            print()
            info(user,id__)
        elif ch == "8":
            login_history(user,"Logged Out",id__)
            clear()
            return
        else:
            print("INVALID CHOICE!")

# MAIN
def main():
    print("=" * 50, "\t  WELCOME TO THE STUDENT BOOK", "=" * 50, sep="\n")
    time.sleep(t+0.2)
    clear()

    while True:
        print("=" * 30, "\t     MENU", "=" * 30, sep="\n")
        print()
        ch = input("Choose an option to proceed:\n\n1) Login\n2) Info\n3) Exit\n")
        if ch =="1":
            clear()
            user = login_menu()
            if user:
                id_= login_history(user,"Logged In")
                if user[3]=="student":
                    clear()
                    student_menu(user,id_)
                elif user[3]=="auditor":
                    clear()
                    audit_menu(user, id_)
            else:
                print("Invalid Credentials")
                time.sleep(t)
                clear()
        elif ch == "2":
            clear()
            print("=" * 30, "\t  PROGRAM INFO", "=" * 30, sep="\n")
            print()
            print("Press 'ENTER' to Return in non-input panels.")
            if input():
                clear()

        elif ch=="3":
            clear()
            exit()
        else:
            return
while True:
    main()