package letiu.pistronics.piston;

import java.util.HashMap;
import letiu.pistronics.util.BlockProxy;
import letiu.pistronics.util.Vector3;

public class ControllerRegistry {
    private static HashMap<String, SystemController> mapClient = new HashMap<String, SystemController>();

    private static HashMap<String, SystemController> mapServer = new HashMap<String, SystemController>();

    public static void register(SystemController ctr, BlockProxy root) {
        HashMap<String, SystemController> map = (root.getWorld()).isRemote ? mapClient : mapServer;
        Vector3 coords = root.getCoords();
        String key = coords.x + "x" + coords.y + "x" + coords.y;
        for (; map.get(key) != null; key = key + "I");
        map.put(key, ctr);
        ctr.setKey(key);
    }

    public static void remove(String key) {
        mapClient.remove(key);
        mapServer.remove(key);
    }

    public static SystemController find(String key) {
        SystemController controller = mapServer.get(key);
        if (controller == null)
            controller = mapClient.get(key);
        return controller;
    }

    public static SystemController create(String key, MoveData moveData, boolean isRemote) {
        HashMap<String, SystemController> map = isRemote ? mapClient : mapServer;
        SystemController ctr = map.get(key);
        if (ctr == null) {
            ctr = new SystemController(moveData);
            map.put(key, ctr);
            ctr.setKey(key);
        }
        return ctr;
    }
}