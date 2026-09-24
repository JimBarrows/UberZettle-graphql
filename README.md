# As a thinking human I want to create, read, update, delete, arrange, group, and utilize ideas So that I can think in my ideas
# What is ZettleKasten?
The Zettlekasten method was popularized by German sociologist Niklas Luhmann—who credited the system for his prolific output of over 70 books and 400 articles—the method mimics how the human brain associates ideas rather than sorting them into rigid folders.  Most, if not all, the books were academic, and many of the articles were for science journals, and probably peer reviewed.  There are three note types: Fleeting, Literature and Permanent.  

Fleeting notes are basically just getting whatever is in your head out of it.  You can use napkins, receipts, a notebook, whatever.  These are todo lists, shopping lists, phone numbers, random ideas you want to remember.  Not every fleeting note is worth keeping though.  You’re just getting things out of your head, and where it’s easier to remember.  Every once in a while (every day, week, whatever), you go through them and curate them.  If they’re worth remembering long term, you put them into your permanent notes.  If they aren’t, you can store them someplace, or toss them in the round file.

Literature notes are basically notes you take while reading.  Today, they’re notes you take while watching youtube, TikTok, or scrolling through facebook, twitter or whatever content your consuming.  They’re probably better named “Content Notes” now, Luhmann lived during the 70’s.  These are also somewhat fleeting.  What goes into your permanent notes is your version of the notes.  Quotes are fine, but most of the notes should be your own words.  Literature notes to permanent notes are rarely 1-1, more like 1 literature note to many permanent notes.  Also on the permanent note, goes a reference back to the literature it came from, so you can build a bibliography.  You typically won’t throw these notes away, since you might need to refer to them again.

Permanent notes are the ideas you want to keep.  On each index card you put a unique identifier, then you go through your existing idea stack and link the new card to as many cards as you can find, using the card identifier and putting it onto the back of the new card.  Luhmann is reported to have 90,000 index cards when he died.   All of them indexed like this.  You add links as you find them, and think about them.

The API is an attempt to create the permanent notes part of his method.  The other two parts of the method don’t need a system necessarily, but they don’t need to be part of this one.  Pen and paper for those do just find, and some research indicates they do better.

Essentially you have Idea[1]->[M]Idea.  An idea is limited to 500 characters (brevity is key).  I used UUID to uniquely identify an idea.  
However, we can add some metadata to the link.  Something like these: 
1) is_part_of
2) contradicts
3) supports
4) parallel_use_cases
5) consequences 
6) source
 at least as a start.  Now we can also start to show the user interesting things about their ideas.

# Why is this UberZettlekasten?
Not because it's an app.  Because of how we can then use the ideas.  A lot of applications, like Obsidain are great at capturing ideas.  They provide great ways to link them.  What they don't do very well is help you think in your ideas.  This is where we'll put the Uber in the Zettle.

We'll be able to assemble notes into a document, while keeping their relationships.  Then we can do whatever we want that document.  Writing a blog?  Great!  Writing a report? Awesome!  Then export the notes to a word processer for final editing, along with the bibliography.

Thinking about a problem, and not sure where to start.  From the links we can build a mind map, in fact this is probably going to be the primary interface.  You'll be able to see where nodes are connected, and how they relate to each other.  You can follow them around until you have something you want to save, at least for the duration of your problem-solving.

# Some things I want to remember about this project
## Bento Boxing
This is a great feature to add https://kurtis-redux.medium.com/what-is-bento-style-note-taking-751df3702068

## Create a graph of ideas
https://medium.com/@ArkProtocol1/postgres-19-adds-graph-queries-i-ran-our-neo4j-workload-against-it-41aab1bc82c5
