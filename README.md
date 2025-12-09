# 🟩⬜ Othello (Reversi) – Android Game

This project is an Android implementation of **Othello (Reversi)**, the classic 8×8 strategy board game where players flip opponent pieces and compete for board control. The app includes full game logic, move validation, score tracking, and simple UI screens built using Android activities and a custom view.

---

## 🎮 Features

- Full Othello game rules implemented
- 8×8 board with valid move detection
- Automatic flipping of opponent tiles
- Turn switching and skip-turn logic
- Game-over detection and winner calculation
- Touch-based gameplay
- Simple UI flow:
  - **MainActivity** – start menu
  - **OthelloActivity** – gameplay screen
  - **EndActivity** – results screen

---

## 🧱 Architecture Overview

- **PlayBoard**  
  Contains the core game logic, board state, valid move checking, flipping logic, and scoring.

- **OthelloView**  
  Custom Android view responsible for drawing the board and responding to player input.

- **MainActivity / OthelloActivity / EndActivity**  
  Standard Android activities handling UI transitions and controlling the game flow.

High-level structure:

MainActivity → OthelloActivity → EndActivity
↑
OthelloView
↓
PlayBoard
