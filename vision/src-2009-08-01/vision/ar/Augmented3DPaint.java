package vision.ar;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.image.BufferedImage;
import javax.media.j3d.Background;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Light;
import javax.media.j3d.PointLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

/**
 *
 * @author Kotuc
 */
public class Augmented3DPaint {

    private final Canvas3D canvas3D;
    private final Craft3D craft = new Craft3D(this);
    private final BranchGroup worldGroup = new BranchGroup();
    private final TransformGroup viewTG;
    private final Background background = new Background();

    /** Creates new form ViewPanel */
    public Augmented3DPaint() {

        this.canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration(), true);
        SimpleUniverse universe = new SimpleUniverse(canvas3D);

//        Enumeration en = universe.getLocale().getAllBranchGraphs();
//        while (en.hasMoreElements()) {
//            Object obj = en.nextElement();
//            System.out.println("obj: " + obj);
//            if (obj instanceof Background) {
//                System.out.println("bg: " + obj);
//            }
//
//        }
        // = universe.getCanvas();

//        universe.getViewingPlatform().setNominalViewingTransform();
        viewTG = universe.getViewingPlatform().getViewPlatformTransform();
        Transform3D t3 = new Transform3D();
        t3.set(new Vector3d(0, 0, 0));
        viewTG.setTransform(t3);

        worldGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);

        Behavior mouseBehR = new MouseRotate(canvas3D, viewTG);
        mouseBehR.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
        worldGroup.addChild(mouseBehR);

        Behavior mouseBehT = new MouseTranslate(canvas3D, viewTG);
        mouseBehT.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
        worldGroup.addChild(mouseBehT);

        Light light = new PointLight(new Color3f(1.0f, 1.0f, 6.0f), new Point3f(1.0f, 1.0f, 0.0f), new Point3f(1.0f, 0.5f, 0.0f));
        light.setInfluencingBounds(new BoundingSphere(new Point3d(), 1000));
        worldGroup.addChild(light);

//        worldGroup.addChild(world.getGroup());
        worldGroup.addChild(craft.getGroup());

        background.setApplicationBounds(new BoundingSphere(new Point3d(), 1000.0));
        background.setColor(0.3f, 0.5f, 0.7f);
        background.setCapability(Background.ALLOW_IMAGE_WRITE);
        worldGroup.addChild(background);

        worldGroup.compile();
        universe.addBranchGraph(worldGroup);
    }

    public BufferedImage getImage(BufferedImage bg) {

//        rott.rotX(Math.random());

        background.setImage(new ImageComponent2D(ImageComponent2D.FORMAT_RGBA, bg));
//        canvas3D.setOffScreenBuffer(new ImageComponent2D(ImageComponent2D.FORMAT_RGB, 320, 240));
        canvas3D.setOffScreenBuffer(new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bg.getWidth(), bg.getHeight()));
//            canvas3D.setSize(320, 240);
        canvas3D.getScreen3D().setSize(bg.getWidth(), bg.getHeight());
        canvas3D.getScreen3D().setPhysicalScreenWidth(0.4);
        canvas3D.getScreen3D().setPhysicalScreenHeight(0.3);

//        System.out.println("screen3D: " + canvas3D.getScreen3D());
        canvas3D.renderOffScreenBuffer();
        canvas3D.waitForOffScreenRendering();
        ImageComponent2D offScreenBuffer = canvas3D.getOffScreenBuffer();
//        System.out.println("buffer: " + offScreenBuffer);
        return offScreenBuffer.getImage();
    }

    public Craft3D getCraft() {
        return craft;
    }
}
