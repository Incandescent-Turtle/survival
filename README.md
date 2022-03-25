# Survival
An unfinished,tile-based, top-down survival game.  
(well like it <i>is</i> playable, there is just no actual <i>game</i> to play. mechanics are there to play around with tho.)   
<a href="https://github.com/Incandescent-Turtle/survival/raw/main/survival.jar">Click here to download the runnable jar file</a>

---
## Features
- scrolling game
- inventory system
- harvestable resources
- building
- interaction with tiles
- animated tiles
- enemies with AI
- animated player
- crafting
- health, hunger, and thirst
- lore
- map building feature

---
## Controls
- WASD for movement  
- E to open inventory  
- Up/Down Arrow to cycle selected item  
- Space to interact and to use current item/build  
- Walk over fruit to pick it up  

### Item Hotkeys
- P for planks
- T for rock
- C for door
- R for hands
- H for scanner
- Q for multi-tool
- F for fruit

### Dev Controls
- O to remove all zombies and spawn 1 at the cursor location
- Left click to spawn zombies (can hold to continually spawn)

---
## Basic Gameplay
Harvest Resources to build and protect yourself agaisnt the zombies. Harvest food and water to stay alive.

### Important Items/Tiles
Some items and tiles have special functions and abilities.  

#### Scanner
![scanner](https://user-images.githubusercontent.com/59327500/160029780-bdfb1da3-0d9c-4864-aa33-12616ec126cf.png)  
Equip the scanner with H, and hover over the mouse over a harvestable tile to see how much material is left.

#### Hands
<img alt="hands" src="https://user-images.githubusercontent.com/59327500/160029682-c102b9d5-5775-4275-8d8b-c1ba45438ebc.png" width="64">

Used to open and close with doors. Equip with R.

#### Multi-tool
<img alt="multi tool" src="https://user-images.githubusercontent.com/59327500/160029635-753b6b90-c03a-44d1-adbc-77c9bf691090.png" width="64">

A tool capable of breaking harvestable and placeable tiles. Hover over the tile to break and hold space to break.

#### Door Tiles
<p float="left">
    <img alt="open door" src="https://user-images.githubusercontent.com/59327500/160029299-538f0df3-8f19-4ff5-b591-ada444584b79.png" width="64">
    <img alt="closed door" src="https://user-images.githubusercontent.com/59327500/160029312-b913fc47-fece-4c37-8371-52fc144d4fcf.png" width="64">
</p>

Doors have two states: open and closed. The player and enemies can pass through an open door but are blocked by closed doors. Use the hand to toggle their state via space bar.

#### Leaves
![leaf](https://user-images.githubusercontent.com/59327500/160029242-36a37949-1c98-42d7-afa2-af74dd8dc08f.png)
![leaf](https://user-images.githubusercontent.com/59327500/160029242-36a37949-1c98-42d7-afa2-af74dd8dc08f.png)
![leaf](https://user-images.githubusercontent.com/59327500/160029242-36a37949-1c98-42d7-afa2-af74dd8dc08f.png)

Leaves have no collision boxes, which means you can pass through them. Enemies and the player are obstructed from view under trees, but only for the player! Leaves will bear different kinds of fruit depending on the tree.

#### Fruit
<p float="left">
  <img alt="apple" src="https://user-images.githubusercontent.com/59327500/160028707-99101136-6cd6-449c-83eb-8f7ac568be6c.png" width="120">
  <img alt="banana" src="https://user-images.githubusercontent.com/59327500/160028810-03b31b16-2d33-454e-b6f8-7a94019d3623.png" width="120">
  <img alt="cherry" src="https://user-images.githubusercontent.com/59327500/160028817-7df9f6fd-594b-46b9-bb35-007a2acfd78e.png" width="120">
  <img alt="lemon" src="https://user-images.githubusercontent.com/59327500/160028830-c5df5354-0d2f-4a6d-922c-b45195790fbd.png" width="120">
  <img alt="orange" src="https://user-images.githubusercontent.com/59327500/160028832-6928d60c-eaf6-4468-bd4b-e764f719eabb.png" width="120">
  <img alt="Peach" src="https://user-images.githubusercontent.com/59327500/160028838-c625d373-cde3-468c-a3da-384747c2ff3d.png" width="120">
  <img alt="pear" src="https://user-images.githubusercontent.com/59327500/160028842-9d246c9c-3965-438c-a2b3-467c2c61f0fc.png" width="120">
</p>
Fruit are items that spawn on leaves and are able to be picked up by the player. When equipped hitting the space bar with consume fruit and replenish the hunger bar.

#### Water
<img alt="water" src="https://user-images.githubusercontent.com/59327500/160030036-3d619985-552b-4371-aa85-e8460b7c1ced.gif" height="64">  
An animated tile that replenishes your thirst when you stand in it.

### Health, Hunger, and Thirst Bar
<img width="121" alt="bars" src="https://user-images.githubusercontent.com/59327500/160028487-259669be-2e1b-4a4c-963f-1b6889d8fb8e.PNG">
The health bar is depleted when an ememy attacks you. It cannot currently be replenished.  
The thirst bar depletes itself overtime and must be refilled by swimming in water.  
The hunger bar depletes itself overtime and must be refilled by consuming fruit.  

