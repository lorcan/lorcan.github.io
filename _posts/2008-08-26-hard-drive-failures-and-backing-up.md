---
layout: post
title: Hard Drive Failures and Backing Up
created: 1219745123
categories:
- lorcan
- hard drive failure
- backing up
- dd_recover
- unison
- subversion
- backup
- subclipse
- rsync
---
I had a hard drive failure two weeks ago. Luckily almost everything on the system was fully backed-up so it wasn't too much of a pain. I had used <a href="http://www.cis.upenn.edu/~bcpierce/unison/docs.html">unison</a> to backup my home directory and the <a href="http://subclipse.tigris.org/">subclipse eclipse plugin</a> to backup my eclipse workspace. The data that were lost (and ultimately mostly salvaged) were the websites on my system, the contents of my mysql databases, as well as data stored in other user accounts on my machine (although I don't take responsibility for these data). I used dd_recover to salvage data from the broken drive and wrote up a <a href="/wiki/public/recoveringfromharddrivefailure">wiki page on the process</a> I used. The process was somewhat successful and most of the important data was salvageable. 

My experiences support the rule that prevention is better than cure. So, with this in mind I've set up a page outlining <a href="/wiki/public/backuppolicies">my current backup policies</a> using rsync, unison, subversion, etc. This page will be grown over the coming weeks with backup information I am using to ensure that my websites and databases are not lost in future. In the spirit of openness I'd like people to take a look at my setup and poke holes in it or make suggestions for improvement. Feel free to reuse my scripts to build your own backup scripts as I have done from others (e.g., <a href="http://www.mikerubel.org/computers/rsync_snapshots/index.html#Appendix">Mike Rubel's rsync scripts</a>). Remember, backup early and backup often, there is no sympathy for anyone who loses data for want of proper backups.
