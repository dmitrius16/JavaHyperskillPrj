package processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class MyMenu {
    private Map<Integer, MenuItem> menuItems = new HashMap<>();
    private List<Integer> orderItems = new ArrayList<>();
    private MyMenu parent;

    public MyMenu(MyMenu parent) {
        this.parent = parent;
    }

    public MyMenu() {
        this.parent = null;
    }

    public MyMenu getParent() {
        return parent;
    }

    public void addItem(String name, MyMenuExecuter exec) {
        int numItemInd = name.indexOf('.');
        if (numItemInd != -1) {
            int numInd = Integer.parseInt(name.substring(0, numItemInd));
            orderItems.add(numInd);
            menuItems.put(numInd, new MenuItem(name, exec));
        }
    }

    public boolean menuHandler(int numItem) {
        if (numItem == 0) {
            //it's exit code
            return true;        // rewrite exit logic in the future
        }

        if (menuItems.containsKey(numItem)) {
            menuItems.get(numItem).executer.executeCmd();
        } else {
            System.out.println("Incorrect menu item");
        }
        return false;
    }

    public void display() {
        for (int el : orderItems) {
            System.out.println(menuItems.get(el).itemName);
        }
    }

    class MenuItem {
        private final String itemName;
        private final MyMenuExecuter executer;

        public MenuItem(String name, MyMenuExecuter exec) {
            itemName = name;
            executer = exec;
        }

        @Override
        public String toString() {
            return itemName;
        }
    }
}
