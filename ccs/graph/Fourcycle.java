package ccs.graph;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashSet;

import ccs.Debug;

/** computes and stores the canonical edges of a TreeDecomp t
 */
public class Fourcycle {

	/** 
	 * @param t	the associated TreeDecomp graph 
	 */
	public Fourcycle(TreeDecomp t) {
		this.t = t;
	}

	TreeDecomp t;
	ArrayList<Edge> canonicalEdges;
	// ArrayList<Pair<Cluster>> l; // adjacent cluster pairs in four-cycles;
	boolean isLow = false;

	/** computes the canonical base non-edges
	 * @return whether the associated TreeDecomp has low Cayley complexity
	 */
	public boolean init() {
		ArrayList<Pair<Cluster>> l = new ArrayList<Pair<Cluster>>();
		canonicalEdges = new ArrayList<Edge>();
		canonicalEdges.add(t.getBaseNonedge());

		HashSet<Cluster> constructedClusters = new HashSet<Cluster>();

		for (int i = 0; i < t.getNumOfConstructStep(); ++i) {
			ConstructionStep s = t.getConstructionStep(i);

			if (i > 0) {
				// (1) U and W
				Vertex v1 = s.v1(), v2 = s.v2();
				HashSet<Cluster> cv1 = new HashSet<Cluster>();
				cv1.addAll(t.getClusters(v1));
				cv1.retainAll(constructedClusters);
				HashSet<Cluster> cv2 = new HashSet<Cluster>();
				cv2.addAll(t.getClusters(v2));
				cv2.retainAll(constructedClusters);

				// (2) find (c1,c2): adjacent base cluster pair
				boolean isLowStep = false;
				Pair<Cluster> pair = null;
				outer: for (Cluster c1 : cv1) {
					for (Cluster c2 : cv2) {
						assert (!c1.equals(c2));
						pair = new Pair<Cluster>(c1, c2);
						if (l.contains(pair)) {
							isLowStep = true;
							break outer; // TODO: pick the pair constructed
											// earliest
						}
					}
				}
				if (!isLowStep) {
					Debug.warnMsg("Step " + s.v() + " makes t not low");
					return false;
				}
				assert (pair != null);
				Vertex v = Cluster.sharedVertex(pair.getFirst(),
						pair.getSecond());
				assert (v != null);
				canonicalEdges.add(new Edge(v, s.v()));

				// (3)
				for (Cluster c1 : cv1) {
					for (Cluster c2 : cv2) {
						assert (!c1.equals(c2));
						if (Cluster.sharedVertex(c1, c2) != null) {
							l.add(new Pair<Cluster>(s.c1(), c1));
							l.add(new Pair<Cluster>(s.c2(), c2));
						}
					}
				}
			}
			l.add(s.clusterPair);
			constructedClusters.add(s.c1());
			constructedClusters.add(s.c2());
		}
		isLow = true;
		return true;

	}

	public AbstractList<Edge> getCanonicalBaseNonedges() {
		assert (isLow);
		return canonicalEdges;
	}

}