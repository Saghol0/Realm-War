<h1 align="center"><strong>Realm War Final Project</strong></h1>

<p align="center">
  <i>Grow your Kingdom and Dominate the Realms</i>
</p>

---

## **Project Introduction**
Welcome to the **Realm War Game** project! This is your chance to create a graphical version of a game.
Your mission? **Domainate Your Opponent's Kingdom Before They Dominate Yours!**

---

## **Game Entities**

### **1. Blocks**

#### ***Void block***
- nothing can be placed inside of a void
- not part of the game

#### ***Empty block***
- an empty space waiting to be absorbed into a kingdom
- if absorbed into the kingdom, it would generate **GOLD** for the kingdom
- if absorbed into the kingdom, a **STRUCTURE** can be built on top of it

#### ***Forest Block***
- it can be a strategic block in **BATTLES** between kingdom **UNITS**
- if absorbed into the kingdom, it would generate **FOOD** for the kingdom
- if absorbed into the kingdom, a **STRUCTURE** can be built on top of it(at the cost of ***losing*** the forest)

> if **UNITS** are starting **ATTACK** from the **FOREST**, they have an advantage of increased **ATTACK POWER**
> if **UNITS** are defending an **ATTACK** while stationed in a **FOREST**, they have an advantage of increased **ATTACK POWER**

### **2. Structures**
- each **STRUCTURE** has a **DURABILITY** of __n__ **HEALTH**
- each **STRUCTURE** has a **MAINTENANCE COST** of __n__ **GOLD**
- each **STRUCTURE** has a **LEVEL** and a **MAX LEVEL**

#### ***Town Hall***
- if conquered, you will **LOSE** the game
- it has no **MAINTENANCE COST**
- it gives a fixed **UNIT SPACE**
- it can produce **GOLD**, **FOOD**, and **UNIT**

**PROPERTIES**
- MAX LEVEL: 1
- MAINTENANCE: 0 GOLD/TURN
- DURABILTY (examlpe: 50 HEALTH POINT)
- GOLD (examlpe: 5 GOLD/TURN)
- FOOD (examlpe: 5 FOOD/TURN)
- UNIT SPACE (examlpe: 5 UNIT SPACE)

#### ***Barrack***
- it can produce **UNIT**
- it produces more **UNIT SPACE** as it **LEVEL UP**
- each kingdom can have a maximum number of **BARRACKS**

**PROPERTIES**
- DURABILTY (examlpe: 50 HEALTH POINT)
- MAINTENANCE COST (examlpe: 5 GOLD/TURN)
- MAX LEVEL (examlpe: 3)
- UNIT SPACE (examlpe: 5 UNIT SPACE)
- BUILDING COST (examlpe: 5 GOLD)
- LEVEL UP COST (examlpe: 5 GOLD -> 10 GOLD)

> each level up gives extra **UNIT SPACE**
> each level up adds to **STRUCTURES's DURABILITY**
> building new **BARRACK** cost more **BUIDING COST** than the previous one (example: PREVIOUS_BUILDING_COST + 5) 

#### ***Farm***
- it can produce **FOOD**
- it produces more **FOOD** as it **LEVEL UP**
- each kingdom can have a maximum number of **FARMS**

**PROPERTIES**
- DURABILTY (examlpe: 50 HEALTH POINT)
- MAINTENANCE COST (examlpe: 5 GOLD/TURN)
- MAX LEVEL (examlpe: 3)
- FOOD (examlpe: 5 FOOD/TURN)
- BUILDING COST (examlpe: 5 GOLD)
- LEVEL UP COST (examlpe: 5 GOLD -> 10 GOLD)

> each level up gives extra **FOOD**
> each level up adds to **STRUCTURE's DURABILITY**
> building new **FARM** costs more **GOLD** than the previous one (example: PREVIOUS_BUILDING_COST + 5) 

#### ***Market***
- it can produce **GOLD**
- it produces more **GOLD** as it **LEVEL UP**
- each kingdom can have a maximum number of **MARKETS**

**PROPERTIES**
- DURABILTY (examlpe: 50 HEALTH POINT)
- MAINTENANCE COST (examlpe: 5 GOLD/TURN)
- MAX LEVEL (examlpe: 3)
- GOLD (examlpe: 5 GOLD/TURN)
- BUILDING COST (examlpe: 5 GOLD)
- LEVEL UP COST (examlpe: 5 GOLD -> 10 GOLD)

> each level up gives an extra **GOLD**
> each level up adds to **STRUCTURES's DURABILITY**
> building new **MARKET** cost more **GOLD** than the previous one (example: PREVIOUS_BUILDING_COST + 5)

#### ***Tower***
- it defends it's neighboring kingdom blocks
- lower level **UNITS** cannot get passed the **TOWER**'s blockade

> example: a level 1 tower can restrict a peasant, a level 2 tower can restrict a spearman and peasant, ...
> you can implement the restriction logic of the towers according to your own preferences

**PROPERTIES**
- DURABILTY (examlpe: 50 HEALTH POINT)
- MAINTENANCE COST (examlpe: 5 GOLD/TURN)
- MAX LEVEL (examlpe: 3)
- BUILDING COST (examlpe: 5 GOLD)
- LEVEL UP COST (examlpe: 5 GOLD -> 10 GOLD)

### **3. Units**
- each **UNIT** has a **HIT POINT** of __n__ **HEALTH**
- each **UNIT** has a **MOVEMENT RANGE** of __n__ **BLOCKS**
- each **UNIT** has an **ATTACK POWER** of __n__ **DAMAGE**
- each **UNIT** has an **ATTACK RANGE** of __n__ **BLOCKS**
- each **UNIT** has a **PAYMENT** of __n__ **GOLD**
- each **UNIT** has a **RATION** of __n__ **FOOD**
- each **UNIT** takes a **UNIT SPACE** of __n__

#### **Unit Hierarchy**
- two **UNIT** of the __same__ **RANK** can be merged to a higher **HIERARCHY**

1. Peasant
2. Spearman
3. Swordman
4. Knight

> you can give each unit, it's own characteristic

---

## **Gameplay Mechanics**

- players take turns in developing there **Kingdom**
- the first player to dominate the **opponent's Kingdom** wins the game

## **Requirements**

### **Object-Oriented Design**
- Proper use of OOP principles:
  - Inheritance and polymorphism for units and structures
  - Encapsulation for entity properties
  - Abstract classes or interfaces for reusable behaviors
- Each game entity (Block, Structure, Unit, etc.) should be implemented as a separate class.

### **Graphics**
- The game must use Swing to build an interactive graphical user interface.
- GUI should allow players to:
  - View the game board and its blocks, structures, and units
  - Click/select blocks to build, upgrade, or deploy units
  - Display real-time game stats (e.g., gold, food, unit capacity)
  - Highlight valid actions (e.g., movable units, buildable structures)
- Animations or visual feedback (e.g., attack effects, unit movement) are encouraged but optional.

### **Turn-Based System**
- The game must support 2 to 4 players in a turn-based format.
- Each player's turn includes:
  - Resource generation (based on current structures and territories)
  - Movement and actions of units
  - Construction or upgrading of structures
- Game logic must enforce turn order and prevent action outside of the player's turn.

### **Database Integration**
- A relational database (e.g., SQLite, MySQL, or PostgreSQL) must be used to store game data.
- Persist the following:
  - Player profiles and statistics
  - Game states (to support saving and loading games)
  - Structure and unit data
  - Logs of in-game actions (for debugging or replays)
- Include functionality for:
  - Saving and loading ongoing games
  - Restarting saved games from the last state

### **Error Handling and Validation**
- The program must handle invalid input and unexpected scenarios gracefully.
- Common errors to manage:
  - Building structures on invalid blocks
  - Exceeding unit space or resources
  - Attacking or moving out of turn
  - Saving or loading corrupted data
- All exceptions should be logged with meaningful messages.

---

## **Optional Stretch Features**
- Not required, but earn extra credit or make the project more impressive:
  - AI Opponent(s): Implement a basic AI for single-player mode.
  - Multiplayer Networked Mode: Using sockets or RMI.
  - Minimap View: A small overview of the whole map.
  - Replay Feature: Load logs to view previous matches step by step.

---

## **Reference**
for more understanding of the project you can checkout **Antiyoy** mobile game

download link: https://play.google.com/store/apps/details?id=yio.tro.antiyoy.android
