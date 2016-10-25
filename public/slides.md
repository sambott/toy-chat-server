class: center, middle, inverse

# Welcome

???

* great to have you all here - thanks for coming etc
* I am Sam, one of the software engineers here. I work on the data ingestion area of our trading pipeline. I predominately work with C# and Scala.

* Gather an idea of experience: how many have been to events like this before etc...
---

.left-column[
  ## Today
  ### - The Challenge
]
.right-column[

## Create a Client for a Simple Chat Server.

With a small group and some inspiration, create something interesting over the course of the afternoon.

You'll be programming against a very simple web api to a chat/instant-messaging service. I'll cover the details soon.

Be inspired, come up with something that sounds interesting to you, and is achievable in a few hours. Have fun!

]

---

.left-column[
  ## Today
  ### - The Challenge
  ### - Some Ideas
]
.right-column[

### Create a chat room client

* Choose an interesting platform:
  * A web app
  * A mobile app
* Maybe add some interesting features:
  * Emoticons
  * `@name` mention alerts

### Create a chat bot

* Create a bot that is able to:
  * Answer your questions
  * Update your Facebook status
  * Play a game(?)

]

---

.left-column[
  ## Today
  ### - The Challenge
  ### - Some Ideas
  ### - What to do
]
.right-column[

### The plan

* Form small groups
* Discuss, agree your idea
* Choose your technology stack and target platform.
* ***Hack!***

I would advise you prototype your ideas as quick as you can. We _all_ like to have the perfect project structure; however, you will gain more from something that works and breaks quickly than having a neat, elegant project structure!

]

---

.left-column[
  ## Today
  ### - The Challenge
  ### - Some Ideas
  ### - What to do
  ### - Then What?
]
.right-column[

### Demo

At the end of the day, you get to show off your work. Nobody is expecting anything refined or fully working, an afternoon is not a lot of time!

* What was your idea?
* What platform and technology stack did you choose? why?
* How far did you get? What works? What doesn't?
* Quick demonstration
* Any cool bits of code you want to show off?!

]

---

.left-column[
  ## API Usage
  ### - Routes
]
.right-column[

### Routes

| Action | Path | Description |
|---|---|---|
| `GET` | `/rooms` | Retrieves a list of the rooms with recent activity |
| `GET` | `/rooms/:room` | Gets most recent messages for the chat room |
| `POST` | `/rooms/:room` | Post a message to the chat room |
| `POST` | `/rooms/:room/ws` | Open a WebSocket for event-driven sending and receiving of messages |

]

---

.left-column[
  ## API Usage
  ### - Routes
  ### - Objects
]
.right-column[

### A sent message:
```js
    {
      user: "your name",
      message: "the message to send"
    }
```

### A received message:
```js
    {
      user: "their name",
      message: "message text",
      dateTime: 1463930717000, // milliseconds since unix epoch
      room: "room_name"
    }
```

]

---

.left-column[
## Links and Starter projects
]
.right-column[

## Seeds:

* [Python](/#/sample/python)
* [AngularJS](/#/sample/angularjs)

## Useful Links:

* [Basic Demo UI](/#/room)
* [PostMan](https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop?hl=en)
* [Dark WebSocket Terminal](https://chrome.google.com/webstore/detail/dark-websocket-terminal/dmogdjmcpfaibncngoolgljgocdabhke?hl=en)
* View the source code on [GitHub](https://github.com/sambott/toy-chat-server)

]

???

Demo API with postman/dwst

---
class: middle, center, inverse

# Enjoy!

We'll be wandering around, if you want someone to bounce ideas off or are struggling with something, or just fancy a chat, flag someone down.

???

Introduce other "mentors"

