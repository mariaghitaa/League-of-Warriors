# README

**Author:** Maria Ghita  
**Group:** 321CC

---

## Project Overview

In my view, the overall difficulty of this project ranged from medium to hard. Integrating the logic from the previous (terminal-based) game with a graphical interface presented several challenges. It took me around 6 days (approximately 3 hours each day) to get everything working smoothly.

---

## Implementation Challenges

- **Game Logic Integration:**  
  Merging the terminal game logic with a graphical user interface was quite challenging. It required rethinking parts of the code to adapt to new visual elements.

- **Fight Pages & Abilities Selection:**  
  Designing the fight pages, creating the ability selection screen, and implementing the character selection required significant attention. One of the toughest parts was developing the **AbilityFrameGUI**, where a maximum of 6 random abilities must be generated, each paired with a suitable image and a selection button.

- **Level Transition and Map Reset:**  
  Transitioning to new levels involved resetting the game map and generating abilities randomly. Each ability needed its own image to ensure a polished look and a dedicated selection button, which added extra complexity.

---

## GUI Components

- **LoginGUI:**  
  This is the initial login window where the player authenticates. Upon successful login, the main game page opens.

- **MainPageGUI:**  
  This window contains the game board. The player's moves trigger the opening of additional windows based on their position on the board.

- **FightFrame:**  
  Displays a battle between two cards: one for the player and one for the enemy. The health and mana values of each are updated based on whether a normal attack or an ability is used. Detailed action messages are also displayed, similar to the terminal version of the game.

- **CharacterSelectionGUI:**  
  This window allows the player to choose their character. It also appears when the player’s character dies, giving them the option to select a new character and continue playing.

- **FinalFrame:**  
  Shows a final status summary of the player's journey. This screen appears both when the player dies (offering the option to restart with a new character) and when the player selects the QUIT option from the main page.

---

## Game Flow

- **Continuous Play:**  
  The game only closes when the player explicitly chooses to exit. If the player’s character dies, the game does not end; instead, the player is given the opportunity to continue by selecting another character.

---

This document provides an overview of the development challenges and the structure of the various GUI components used in the game. Feel free to modify and expand this README as needed for your project documentation.
