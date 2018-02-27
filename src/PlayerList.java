import javax.swing.*;

public class PlayerList extends DefaultListModel<Player> {
    public PlayerList() {
        super();
    }

    public void addPlayer (int id, String name, int money, boolean canPlay){
        super.addElement(new Player(id, name, money, canPlay));
    }

    // Find a player by name.
    public Player findPlayer(String name){
        Player player;
        int index = -1;
        for(int i = 0; i < super.size(); i++){
            player = (Player)super.elementAt(i);
            if(player.getName().equals(name)){
                index = i;
                break;
            }
        }

        if(index == -1) {
            return null;
        }
        else{
            return (Player)super.elementAt(index);
        }
    }
    // Find a player by ID instead, without needing to remember different method names. (findPlayer will accept either int or String)
    public Player findPlayer(int id){
        Player player;
        int index = -1;
        for(int i = 0; i < super.size(); i++){
            player = (Player)super.elementAt(i);
            if(player.getId() == id){
                index = i;
                break;
            }
        }

        if(index == -1) {
            return null;
        }
        else{
            return (Player)super.elementAt(index);
        }
    }

    public void removePlayer (int id){
        Player toRemove = this.findPlayer (id);
        super.removeElement(toRemove);

    }
}
