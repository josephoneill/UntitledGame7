package com.joneill.unnamed7.desktop.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by josep_000 on 4/26/2016.
 */
public class Rectangle {
    public Vector2 bLeftVertex;
    public Vector2 tLeftVertex;
    public Vector2 tRightVertex;
    public Vector2 bRightVertex;
    public Vector2 center;

    public float width;
    public float height;

    public boolean rotatesOnYAxis;
    public boolean pivotsOnOrigin;

    //Offset from given x and y of object
    public Vector2 boundsOffset;
    //If doesn't pivot on origin
    public Vector2 pivotOffset;

    public Rectangle(Vector2 bLeftVertex, Vector2 tLeftVertex, Vector2 tRightVertex, Vector2 bRightVertex, Vector2 parentPos, boolean rotatesOnYAxis
            , boolean pivotsOnOrigin) {
        this.bLeftVertex = bLeftVertex;
        this.tLeftVertex = tLeftVertex;
        this.tRightVertex = tRightVertex;
        this.bRightVertex = bRightVertex;
        this.width = bRightVertex.x - bLeftVertex.x;
        this.height = tLeftVertex.y - bLeftVertex.y;
        this.rotatesOnYAxis = rotatesOnYAxis;
        this.pivotsOnOrigin = pivotsOnOrigin;
        pivotOffset = new Vector2();
        boundsOffset = new Vector2(bLeftVertex.x - parentPos.x, bLeftVertex.y - parentPos.y);
        center = new Vector2(bLeftVertex.x + ((bRightVertex.x - bLeftVertex.x) / 2.0f),
                bLeftVertex.y + ((tLeftVertex.y - bLeftVertex.y) / 2.0f));
    }

    public Rectangle(float x, float y, float width, float height, Vector2 parentPos, boolean rotatesOnYAxis, boolean pivotsOnOrigin) {
        this.width = width;
        this.height = height;
        this.rotatesOnYAxis = rotatesOnYAxis;
        this.pivotsOnOrigin = pivotsOnOrigin;
        bLeftVertex = new Vector2(x, y);
        tLeftVertex = new Vector2(x, y + height);
        tRightVertex = new Vector2(x + width, y + height);
        bRightVertex = new Vector2(x + width, y);
        pivotOffset = new Vector2();
        boundsOffset = new Vector2(bLeftVertex.x - parentPos.x, bLeftVertex.y - parentPos.y);
        center = new Vector2(x + (width / 2.0f), y + (height / 2.0f));
    }

    public void updatePosition(float x, float y, float angle) {
        x = x + boundsOffset.x;
        y = y + boundsOffset.y;
        bLeftVertex.set(x, y);
        tLeftVertex.set(x, y + height);
        tRightVertex.set(x + width, y + height);
        bRightVertex.set(x + width, y);
        if (pivotsOnOrigin) {
            center.set(x + width / 2.0f, y + height / 2.0f);
        } else {
            center.set(x + pivotOffset.x, y + pivotOffset.y);
        }
        if (rotatesOnYAxis) {
            rotateTo(angle);
        }
    }

    public void rotateTo(float angle) {
        bLeftVertex.set(rotateVertex(bLeftVertex, center, angle));
        tLeftVertex.set(rotateVertex(tLeftVertex, center, angle));
        tRightVertex.set(rotateVertex(tRightVertex, center, angle));
        bRightVertex.set(rotateVertex(bRightVertex, center, angle));
    }

    private Vector2 rotateVertex(Vector2 vertex, Vector2 rotatePoint, float theta) {
        float originX = vertex.x - rotatePoint.x;
        float originY = vertex.y - rotatePoint.y;

        float rotatedX = originX * MathUtils.cosDeg(theta) - originY * MathUtils.sinDeg(theta) + rotatePoint.x;
        float rotatedY = originX * MathUtils.sinDeg(theta) + originY * MathUtils.cosDeg(theta) + rotatePoint.y;

        return new Vector2(rotatedX, rotatedY);
    }

    //Math is based on article: www.gamedev.net/page/resources/_/technical/game-programming/
    //2d-rotated-rectangle-collision-r2604
    public boolean collidesWithRect(Rectangle b) {
        if (rotatesOnYAxis) {
            Vector2 axis1 = new Vector2(this.gettRightVertex().x - this.gettLeftVertex().x,
                    this.gettRightVertex().y - this.gettLeftVertex().y);
            Vector2 axis2 = new Vector2(this.gettRightVertex().x - this.getbRightVertex().x,
                    this.gettRightVertex().y - this.getbRightVertex().y);
            Vector2 axis3 = new Vector2(b.gettLeftVertex().x - b.getbLeftVertex().x,
                    b.gettLeftVertex().y - b.getbLeftVertex().y);
            Vector2 axis4 = new Vector2(b.gettLeftVertex().x - b.gettRightVertex().x,
                    b.gettLeftVertex().y - b.gettRightVertex().y);

            if (!isColOnAxis(this, b, axis1)) {
                return false;
            }
            if (!isColOnAxis(this, b, axis2)) {
                return false;
            }
            if (!isColOnAxis(this, b, axis3)) {
                return false;
            }
            if (!isColOnAxis(this, b, axis4)) {
                return false;
            }
            return true;
        } else {
            return (Math.abs(this.getbLeftVertex().x - b.getbLeftVertex().x) * 2
                    < (this.getWidth() + b.getWidth()) && (Math.abs(this.getbLeftVertex().y - b.getbLeftVertex().y)) * 2
                    < (this.getHeight() + b.getHeight()));
        }
    }

    private boolean isColOnAxis(Rectangle a, Rectangle b, Vector2 axis) {
        float aURVal = getPVertexPosition(projectVectorOnAxis(a.gettRightVertex(), axis), axis);
        float aLRVal = getPVertexPosition(projectVectorOnAxis(a.getbRightVertex(), axis), axis);
        float aULVal = getPVertexPosition(projectVectorOnAxis(a.gettLeftVertex(), axis), axis);
        float aLLVal = getPVertexPosition(projectVectorOnAxis(a.getbLeftVertex(), axis), axis);

        Float[] aMinMax = new Float[4];
        aMinMax[0] = aURVal;
        aMinMax[1] = aLRVal;
        aMinMax[2] = aULVal;
        aMinMax[3] = aLLVal;

        Arrays.sort(aMinMax);

        float bURVal = getPVertexPosition(projectVectorOnAxis(b.gettRightVertex(), axis), axis);
        float bLRVal = getPVertexPosition(projectVectorOnAxis(b.getbRightVertex(), axis), axis);
        float bULVal = getPVertexPosition(projectVectorOnAxis(b.gettLeftVertex(), axis), axis);
        float bLLVal = getPVertexPosition(projectVectorOnAxis(b.getbLeftVertex(), axis), axis);

        Float[] bMinMax = new Float[4];
        bMinMax[0] = bURVal;
        bMinMax[1] = bLRVal;
        bMinMax[2] = bULVal;
        bMinMax[3] = bLLVal;

        Arrays.sort(bMinMax);

        if (bMinMax[0] <= aMinMax[3] && bMinMax[3] >= aMinMax[0]) {
            return true;
        }
        return false;
    }

    private Vector2 projectVectorOnAxis(Vector2 vertex, Vector2 axis) {
        Vector2 projectedVertex = new Vector2();
        float numerator = vertex.x * axis.x + vertex.y * axis.y;
        float denominator = axis.x * axis.x + axis.y * axis.y;
        float quotient = numerator / denominator;
        float x = quotient * axis.x;
        float y = quotient * axis.y;

        projectedVertex.set(x, y);
        return projectedVertex;
    }

    public void renderBounds(ShapeRenderer renderer) {
        renderer.line(bLeftVertex.x, bLeftVertex.y, tLeftVertex.x, tLeftVertex.y);
        renderer.line(tLeftVertex.x, tLeftVertex.y, tRightVertex.x, tRightVertex.y);
        renderer.line(tRightVertex.x, tRightVertex.y, bRightVertex.x, bRightVertex.y);
        renderer.line(bRightVertex.x, bRightVertex.y, bLeftVertex.x, bLeftVertex.y);
        renderer.circle(center.x, center.y, 0.05f);
    }

    private float getPVertexPosition(Vector2 pVertex, Vector2 axis) {
        return pVertex.x * axis.x + pVertex.y * axis.y;
    }

    public Vector2 getbLeftVertex() {
        return bLeftVertex;
    }

    public void setbLeftVertex(Vector2 bLeftVertex) {
        this.bLeftVertex = bLeftVertex;
    }

    public Vector2 gettLeftVertex() {
        return tLeftVertex;
    }

    public void settLeftVertex(Vector2 tLeftVertex) {
        this.tLeftVertex = tLeftVertex;
    }

    public Vector2 gettRightVertex() {
        return tRightVertex;
    }

    public void settRightVertex(Vector2 tRightVertex) {
        this.tRightVertex = tRightVertex;
    }

    public Vector2 getbRightVertex() {
        return bRightVertex;
    }

    public void setbRightVertex(Vector2 bRightVertex) {
        this.bRightVertex = bRightVertex;
    }

    public Vector2 getCenter() {
        return center;
    }

    public void setCenter(Vector2 center, Vector2 start, boolean pivotOnOrigin) {
        if (!pivotOnOrigin) {
            pivotOffset.x = center.x - start.x;
            pivotOffset.y = center.y - start.y;
            System.out.println("Center" + pivotOffset.x);
        }
        this.center = center;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isRotatesOnYAxis() {
        return rotatesOnYAxis;
    }

    public void setRotatesOnYAxis(boolean rotatesOnYAxis) {
        this.rotatesOnYAxis = rotatesOnYAxis;
    }

    public boolean isPivotsOnOrigin() {
        return pivotsOnOrigin;
    }

    public void setPivotsOnOrigin(boolean pivotsOnOrigin) {
        this.pivotsOnOrigin = pivotsOnOrigin;
    }

    public Vector2 getPivotOffset() {
        return pivotOffset;
    }

    public void setPivotOffset(Vector2 pivotOffset) {
        this.pivotOffset = pivotOffset;
    }
}
