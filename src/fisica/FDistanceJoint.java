package fisica;

import processing.core.*;

import java.util.ArrayList;

import org.jbox2d.common.*;
import org.jbox2d.collision.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.joints.*;

/**
 * Represents a distance joint that tries to keep two bodies at a constant distance.  This joint is similar to connecting both bodies by a spring.
 *
 */
public class FDistanceJoint extends FJoint {
  protected FBody m_body1;
  protected FBody m_body2;
  protected float m_damping = 0.3f;
  protected float m_frequency = 10.0f;
  protected float m_length;

  protected Vec2 m_anchor1;
  protected Vec2 m_anchor2;

  protected JointDef getJointDef(FWorld world) {

    DistanceJointDef md = new DistanceJointDef();
    md.body1 = m_body1.m_body;
    md.body2 = m_body2.m_body;
    md.localAnchor1 = m_anchor1.clone();
    md.localAnchor2 = m_anchor2.clone();
    md.length = Fisica.screenToWorld(m_length);
    md.frequencyHz = m_frequency;
    md.dampingRatio = m_damping;
    m_body1.m_body.wakeUp();
    m_body2.m_body.wakeUp();

    return md;
  }

  /**
   * Construct a distance joint between two bodies.  The constructor automatically sets the anchors of the joint to the centers of each body, and the length of the joint to the current distance between the bodies.
   *
   * @param body1  first body of the joint
   * @param body2  second body of the joint
   */
  public FDistanceJoint(FBody body1, FBody body2) {
    super();
    m_body1 = body1;
    m_body2 = body2;

    // These local positions ( relative to the bodys' positions )
    m_anchor1 = new Vec2(0.0f, 0.0f);
    m_anchor2 = new Vec2(0.0f, 0.0f);

    calculateLength();
    //m_length = m_body1.m_body.getPosition().add(m_anchor1).sub(m_body2.m_body.getPosition().add(m_anchor2)).length();
  }

  /**
   * Sets the damping of the spring used to maintain the distance between the bodies constant.
   *
   * @param damping  the damping of the spring
   */
  public void setDamping(float damping) {
    if ( m_joint != null ) {
      ((DistanceJoint)m_joint).m_dampingRatio = damping;
    }

    m_damping = damping;
  }

  /**
   * Sets the frequency of the spring used to maintain the distance between the bodies constant.
   *
   * @param frequency  the frequency of the spring
   */
  public void setFrequency(float frequency) {
    if ( m_joint != null ) {
      ((DistanceJoint)m_joint).m_frequencyHz = frequency;
    }

    m_frequency = frequency;
  }

  /**
   * Sets the length of the joint to the current distance between the bodies.
   *
   */
  public void calculateLength() {
    float lengthX = ((m_body1.getX() + getAnchor1X()) - (m_body2.getX() + getAnchor2X()));
    float lengthY = ((m_body1.getY() + getAnchor1Y()) - (m_body2.getY() + getAnchor2Y()));
    setLength((float)Math.sqrt(lengthX*lengthX + lengthY*lengthY));
    /*
    float world_length = m_body1.m_body.getPosition().add(m_anchor1).sub(m_body2.m_body.getPosition().add(m_anchor2)).length();
    
    if ( m_joint != null ) {
      ((DistanceJoint)m_joint).m_length = world_length;
    }

    m_length = world_length;
    */
  }

  /**
   * Sets the target distance for the joint.  This is the distance that the joint will try to maintain between the two bodies.  If you want to use as length the current distance between the two bodies, use the method {@link #calculateLength() calculateLength}.
   *
   * @param length  the length of the joint
   */
  public void setLength(float length) {
    if ( m_joint != null ) {
      ((DistanceJoint)m_joint).m_length = Fisica.screenToWorld(length);
    }

    m_length = length;
  }


  /**
   * Sets the position of the anchor of the second end of the joint on the second body.  This position must be given relative to the center of the second body.  The anchor point is the point used to calculate the distance and to apply forces in order to move the body.
   *
   * @param x  the horizontal coordinate of the second anchor relative to the center of the second body
   * @param y  the vertical coordinate of the second anchor relative to the center of the second body
   */
  public void setAnchor2(float x, float y) {
    if (m_joint != null) {
      ((DistanceJoint)m_joint).getAnchor2().set(Fisica.screenToWorld(x), Fisica.screenToWorld(y));
    }

    m_anchor2 = Fisica.screenToWorld(x, y);
  }

  /**
   * Sets the position of the anchor of the first end of the joint on the first body.  This position must be given relative to the center of the first body.  The anchor point is the point used to calculate the distance and to apply forces in order to move the body.
   *
   * @param x  the horizontal coordinate of the first anchor relative to the center of the first body
   * @param y  the vertical coordinate of the first anchor relative to the center of the first body
   */
  public void setAnchor1(float x, float y) {
    if (m_joint != null) {
      ((DistanceJoint)m_joint).getAnchor1().set(Fisica.screenToWorld(x), Fisica.screenToWorld(y));
    }

    m_anchor1 = Fisica.screenToWorld(x, y);
  }

  /**
   * Get the horizontal coordinate of the first anchor point on the first body.  This position is given relative to the center of the first body.
   *
   * @return  the horizontal coordinate of the first anchor relative to the center of the first body
   */
  public float getAnchor1X() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getAnchor1()).x;
    }

    return Fisica.worldToScreen(m_anchor1.x);
  }

  /**
   * Get the vertical coordinate of the first anchor point on the first body.  This position is given relative to the center of the first body.
   *
   * @return  the vertical coordinate of the first anchor relative to the center of the first body
   */
  public float getAnchor1Y() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getAnchor1()).y;
    }

    return Fisica.worldToScreen(m_anchor1.y);
  }

  /**
   * Get the horizontal coordinate of the second anchor point on the second body.  This position is given relative to the center of the second body.
   *
   * @return  the horizontal coordinate of the second anchor relative to the center of the second body
   */
  public float getAnchor2X() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getAnchor2()).x;
    }

    return Fisica.worldToScreen(m_anchor2.x);
  }

  /**
   * Get the vertical coordinate of the second anchor point on the second body.  This position is given relative to the center of the second body.
   *
   * @return  the vertical coordinate of the second anchor relative to the center of the second body
   */
  public float getAnchor2Y() {
    if (m_joint != null) {
      return Fisica.worldToScreen(m_joint.getAnchor2()).y;
    }

    return Fisica.worldToScreen(m_anchor2.y);
  }

  public void draw(PApplet applet){
    preDraw(applet);

    applet.ellipse(getAnchor1X(), getAnchor1Y(), 5, 5);
    applet.line(getAnchor1X(), getAnchor1Y(), getAnchor2X(), getAnchor2Y());
    applet.ellipse(getAnchor2X(), getAnchor2Y(), 5, 5);
    
    postDraw(applet);
  }
}