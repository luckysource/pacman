package pacman;

import javax.swing.JFrame;

import pacman.Board;


@SuppressWarnings("serial")
public class Pacman extends JFrame
{

  public Pacman()
  {
    add(new Board());
    setTitle("Pacman");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    //setSize(380, 420);
    setSize(760,840);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public static void main(String[] args) {
      new Pacman();
  }
}