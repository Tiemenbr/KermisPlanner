package Gui;

import Gui.SimulatorView.MapGenerator;
import Gui.SimulatorView.SpriteSheetHelper;
import Gui.SimulatorView.Visitor;
import Objects.Location;
import Objects.Schedule;
import javafx.animation.AnimationTimer;
import javafx.scene.input.ScrollEvent;
import org.jfree.fx.FXGraphics2D;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import org.jfree.fx.ResizableCanvas;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Simulator{
    // TODO: fix zooming bug (not centered in the middle of the screen)
    private static Schedule schedule = Planner.getSCHEDULE();
    private static Canvas canvas;
    private static StackPane stackPane;
    private static Camera camera;
    private static final MapGenerator mapGenerator = new MapGenerator("testDrive.json");
    static ArrayList<Visitor> visitors = new ArrayList<>();
    private static SpriteSheetHelper spriteSheetHelper;
    private static int visitorAmount = 100;
    private static ArrayList<Location> locations = new ArrayList<>();

    public static StackPane getComponent() {
        stackPane = new StackPane();
        canvas = new ResizableCanvas(g -> draw(g), stackPane);
        FXGraphics2D g2d = new FXGraphics2D(canvas.getGraphicsContext2D());
        draw(g2d); // Draw your content initially

        camera = new Camera(canvas);
        stackPane.getChildren().add(canvas);

        // Handle mouse events for panning
        stackPane.setOnMousePressed(event -> camera.handleMousePressed(event));
        stackPane.setOnMouseDragged(event -> camera.handleMouseDragged(event));

        // Handle scroll event for zooming
        stackPane.setOnScroll(event -> camera.handleScroll(event));
        canvas.setOnMouseMoved(event -> {
            for (Visitor visitor : visitors) {
                visitor.setTargetPosition(new Point2D.Double(event.getX(), event.getY()));
            }
        });
        new AnimationTimer() {
            long last = -1;
            int frameCount = 0;
            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                if (frameCount%100 == 1){
                    addVisitor();
                }
                last = now;
                draw(g2d);
                frameCount++;
            }
        }.start();
        init();
        return stackPane;
    }

    public static void init() {
        // Get all locations
        locations.addAll(schedule.getLocations().values());

        spriteSheetHelper = new SpriteSheetHelper();
//        BufferedImage[] vistorSprites1 = spriteSheetHelper.createSpriteSheet("/walk template 2.png", 4);

        while(visitors.size() < 3) {
            addVisitor();
        }
    }

    private static void addVisitor(){
        if (visitors.size() < visitorAmount){
            Point2D newPosition = new Point2D.Double(canvas.getWidth()/2+Math.random()*100, canvas.getHeight()-Math.random()*100);
            boolean hasCollision = false;
            for (Visitor visitor : visitors) {
                if(visitor.getPosition().distance(newPosition) < visitor.getHitBoxSize())
                    hasCollision = true;
            }
            if(!hasCollision) {
                visitors.add(new Visitor(newPosition, 0));
            } else{
                addVisitor();
            }
        }
    }

    private static void draw(FXGraphics2D g2d){
        mapGenerator.draw(g2d);
        for (Visitor visitor : visitors) {
            visitor.draw(g2d);
        }
        for (Location location : locations){
            location.draw(g2d);
        }
    }

    private static void update(double deltaTime){
        // Get scale factors based on screen size
        double cacheImageWidth = mapGenerator.getCacheImageWidth();
        double cacheImageHeight = mapGenerator.getCacheImageHeight();
        double scaleFactorWidth = stackPane.getWidth()/cacheImageWidth;
        double scaleFactorHeight = stackPane.getHeight()/cacheImageHeight;

        // Transform the cacheimage
        AffineTransform tx = new AffineTransform();
//        tx.scale(scaleFactorWidth, scaleFactorHeight);
        canvas.setHeight(cacheImageHeight);
        canvas.setWidth(cacheImageWidth);
        canvas.setScaleX(camera.scale + tx.getScaleX());
        canvas.setScaleY(camera.scale + tx.getScaleY());
        for (Visitor visitor : visitors) {
            visitor.update(visitors, mapGenerator.getCollisionLayer(), deltaTime);
        }
    }
    private static final double DEFAULT_SCALE = 1.0;
    private static final double ZOOM_FACTOR = 0.1;

    public static class Camera {
        private Canvas canvas;
        private double offsetX = 0;
        private double offsetY = 0;
        private double scale = DEFAULT_SCALE;

        public Camera(Canvas canvas) {
            this.canvas = canvas;
        }

        public void handleMousePressed(javafx.scene.input.MouseEvent event) {
            offsetX = event.getX();
            offsetY = event.getY();
        }

        public void handleMouseDragged(javafx.scene.input.MouseEvent event) {
            double deltaX = event.getX() - offsetX;
            double deltaY = event.getY() - offsetY;
            offsetX = event.getX();
            offsetY = event.getY();
            canvas.setTranslateX(canvas.getTranslateX() + deltaX);
            canvas.setTranslateY(canvas.getTranslateY() + deltaY);
        }

        public void handleScroll(ScrollEvent event) {
            double delta = event.getDeltaY();
            if (delta < 0) {
                scale -= ZOOM_FACTOR;
            } else {
                scale += ZOOM_FACTOR;
            }
            if (scale < 0){
                scale = -scale;
            }
            canvas.setScaleX(scale);
            canvas.setScaleY(scale);
            event.consume();
        }
    }
}