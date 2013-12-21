package ccs;

import ccs.graph.Graph;
import ccs.graph.Point2D;

public class BoundingBox {

	double x_min, x_max, y_min, y_max;
	
	public double getWidth(){
		return x_max - x_min;
	}
	
	public double getHeight(){
		return y_max - y_min;
	}

	public BoundingBox(double x1, double x2, double y1, double y2) {
		x_min = x1;
		x_max = x2;
		y_min = y1;
		y_max = y2;
	}

	public BoundingBox(Graph g) {
		x_min = Double.POSITIVE_INFINITY;
		x_max = Double.NEGATIVE_INFINITY;
		y_min = Double.POSITIVE_INFINITY;
		y_max = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < g.getSize(); ++i) {
			Point2D p = g.getPoint(g.getVertex(i));
			if (p.x() < x_min)
				x_min = p.x();
			if (p.x() > x_max)
				x_max = p.x();
			if (p.y() < y_min)
				y_min = p.y();
			if (p.y() > y_max)
				y_max = p.y();
		}
	}
	
	public String toString(){
		return "["+x_min+","+x_max+";"+y_min+","+y_max+"]";
	}
}