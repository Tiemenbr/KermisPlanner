package Gui.SimulatorView;

import Gui.Planner;
import Objects.Location;

import javax.imageio.ImageIO;
import javax.json.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class MapGenerator {

    private BufferedImage tilemap;
    //    private int layerWidth;
//    private int layerHeight;
    private int[][] layerMap;
    private ArrayList<TileLayer> layers = new ArrayList<>();
    private int tileHeight;
    private int tileWidth;
    private ArrayList<BufferedImage> tiles = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();

    public MapGenerator(String fileName) {
        JsonReader reader = Json.createReader(getClass().getClassLoader().getResourceAsStream(fileName));
        JsonObject root = reader.readObject();

        try {
            // Find the tileset
            JsonArray list = root.getJsonArray("tilesets");
            JsonObject item = list.getJsonObject(0);
            String imagePath = item.getString("image");
            String[] imagePathItems = imagePath.split("/");
            String image = "";
            for (String imagePathItem : imagePathItems) {
                //System.out.println(imagePathItem);
                if (imagePathItem.contains("/png")) ;
                {
                    image = imagePathItem;
                }
            }
            if (!image.equalsIgnoreCase("")) {
                this.tilemap = ImageIO.read(getClass().getClassLoader().getResourceAsStream(image));
            }

            // OLD
            //JsonObject item = list.getJsonObject(1);
            //String source = item.getString("source");
            //System.out.println(source);
            //this.tilemap = ImageIO.read(getClass().getResourceAsStream(source));

            // Get Tilewidth & Tileheight
            if (root.containsKey("tilewidth")) {
                this.tileWidth = root.getInt("tilewidth");
            }
            if (root.containsKey("tileheight")) {
                this.tileHeight = root.getInt("tileheight");
            }

            // Get tile images
            if (this.tileWidth != 0 && this.tileHeight != 0) {
                for (int y = 0; y < tilemap.getHeight(); y += tileHeight) {
                    for (int x = 0; x < tilemap.getWidth(); x += tileWidth) {
                        tiles.add(tilemap.getSubimage(x, y, tileWidth, tileHeight));
                    }
                }

                // Initialize & Get layer values
                String tileType = "";
                String layerName = "";
                int layerWidth = 0;
                int layerHeight = 0;
                JsonArray layers = root.getJsonArray("layers");
                for (int i = 0; i < layers.size(); i++) {
                    // Get layer name
                    if (layers.getJsonObject(i).containsKey("name")) {
                        layerName = layers.getJsonObject(i).getString("name");
                    }
                    // Get layer type
                    if (layers.getJsonObject(i).containsKey("type")) {
                        tileType = layers.getJsonObject(i).getString("type");

                        // Get all objects from objectgroup
                        if (tileType.equalsIgnoreCase("objectgroup")){
                            int objectAmount = layers.getJsonObject(i).getJsonArray("objects").size();
                            for (int j = 0; j < objectAmount; j++){
                                String name = layers.getJsonObject(i).getJsonArray("objects").getJsonObject(j).getString("name");
                                if (name.equalsIgnoreCase("")){
                                    name = layers.getJsonObject(i).getJsonArray("objects").getJsonObject(j).getString("type");
                                }
                                int height = layers.getJsonObject(i).getJsonArray("objects").getJsonObject(j).getInt("height");
                                int width = layers.getJsonObject(i).getJsonArray("objects").getJsonObject(j).getInt("width");
                                int x = layers.getJsonObject(i).getJsonArray("objects").getJsonObject(j).getInt("x");
                                int y = layers.getJsonObject(i).getJsonArray("objects").getJsonObject(j).getInt("y");

                                locations.add(new Location(height, width, name, new Point2D.Double(x, y)));
                                Planner planner = new Planner();
                                planner.getSCHEDULE().getLocations();
                            }
                        }
                    }
                    // Get layer width
                    if (layers.getJsonObject(i).containsKey("width")) {
                        layerWidth = layers.getJsonObject(i).getInt("width");
                    }
                    // Get layer Height
                    if (layers.getJsonObject(i).containsKey("height")) {
                        layerHeight = layers.getJsonObject(i).getInt("height");
                    }
                    // Get layer data (array)
                    if (layers.getJsonObject(i).containsKey("data")) {
                        JsonArray arrayData = layers.getJsonObject(i).getJsonArray("data");
                        this.layerMap = new int[layerHeight][layerWidth];

                        for (int y = 0; y < layerHeight; y++) {

                            int[] oneLayer = new int[layerWidth];
                            for (int x = 0; x < layerWidth; x++) {
                                int oneItem = arrayData.getInt(x + (layerWidth * y));
                                oneLayer[x] = oneItem;
                            }
                            this.layerMap[y] = oneLayer;
                        }
                    }

                    // Add layer to ArrayList
                    TileLayer tileLayer = new TileLayer(tileType, layerWidth, layerHeight, this.layerMap, this.tileWidth, this.tileHeight, this.tiles,layerName);
                    this.layers.add(tileLayer);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException!");
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2d) {
        // Draw each individual layer
        for (TileLayer layer : layers) {
            layer.draw(g2d);
        }
        // Draw objects
        for (Location location : locations){
            location.draw(g2d);
        }
    }

    public double getCacheImageWidth() {
        double width = 0;
        for (TileLayer layer : layers) {
            if (layer.hasCacheImage()) {
                width = layer.getCacheImageWidth();
            }
        }
        return width;
    }

    public double getCacheImageHeight() {
        double height = 0;
        for (TileLayer layer : layers) {
            if (layer.hasCacheImage()) {
                height = layer.getCacheImageHeight();
            }
        }
        return height;
    }

    public TileLayer getCollisionLayer() {
        for (TileLayer layer : layers) {
            if (layer.getName().equalsIgnoreCase("collisionlayer")) {
                return layer;
            }
        }
        return null;
    }
}