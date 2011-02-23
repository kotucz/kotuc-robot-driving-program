package vision.ar;

import com.sun.j3d.utils.geometry.Box;
import java.util.Enumeration;
import javax.media.j3d.Appearance;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Group;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 *
 * @author Kotuc
 */
public class Craft3D {

    private TransformGroup posTG = new TransformGroup();
    private TransformGroup rotTG = new TransformGroup();
    Augmented3DPaint painter;
    private Transform3D post = new Transform3D();
    private Transform3D rott = new Transform3D();
    private BranchGroup root = new BranchGroup();

    public Craft3D(Augmented3DPaint painter) {
        super();
        this.painter = painter;
        Appearance appear = new Appearance();
        appear.setMaterial(new Material());
        //rotTG.addChild(new ColorCube(0.5));
        //rotTG.addChild(new Box(1f, 0.75f, 0.01f, new Appearance()));
        root.setCapability(BranchGroup.ALLOW_DETACH);
        root.addChild(posTG);
        rotTG.addChild(new Box(0.5F, 0.5F, 0.01F, appear));
        rotTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        posTG.addChild(rotTG);
        posTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        posTG.setCapability(TransformGroup.ALLOW_COLLISION_BOUNDS_READ);
        Behavior beh = new InnerBehavior();
        beh.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000));
        rotTG.addChild(beh);
        posTG.getCollisionBounds();
    }

    public void setPos(Point3d pos) {
        post.set(new Vector3d(pos));
        posTG.setTransform(post);
    }

    public void setRot(Transform3D rott) {
        this.rott = rott;
        rotTG.setTransform(rott);
    }

    public Group getGroup() {
        return root;
    }

    class InnerBehavior extends Behavior {

        private WakeupCondition cond = new WakeupOnElapsedFrames(0);

        @Override
        public void initialize() {
            wakeupOn(cond);
        }

        @Override
        public void processStimulus(Enumeration arg0) {
//            System.out.println("stimulus");
//            craft.setThrust(0, downPressed+leftPressed);
//            craft.setThrust(1, downPressed+rightPressed);
//            craft.setThrust(2, upPressed+leftPressed);
//            craft.setThrust(3, upPressed+rightPressed);
//                posTG.setTransform(getPosTransform());
//                rotTG.setTransform(getRotTransform());
            wakeupOn(cond);
        }
    }
}
