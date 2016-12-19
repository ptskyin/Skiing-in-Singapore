import java.util.ArrayList;

/**
 * Created by skyin on 12/18/2016.
 */
public class SkiNode {
	public final int v; // height on map
	public final int i;
	public final int j;
	public int level; // max of length to the min poses (tail) it can reach
	public final SkiNode prev;
	public SkiNode[] next = new SkiNode[4];


	public SkiNode(int v, int i, int j, SkiNode prev) {
		this.v = v;
		this.i = i;
		this.j = j;
		this.prev = prev;
	}

	public SkiNode(SkiNode node) {
		v = node.v;
		i = node.i;
		j = node.j;
		level = node.level;
		prev = null;
	}

	public void print() {
		ArrayList<SkiNode> list = new ArrayList<>();
		list.add(this);
		while (list.size() != 0) {
			String s = "";
			ArrayList<SkiNode> nList = new ArrayList<>();
			for (SkiNode skiNode : list) {
				s += skiNode.v + "(" + skiNode.level + ") ";
				for (SkiNode nextNode : skiNode.next) {
					if (nextNode != null) {
						nList.add(nextNode);
					}
				}
			}
			list = nList;
			System.out.println(s);
		}
	}

	public void printBackward() {
		String s = "";
		SkiNode node = this;
		while (node != null) {
			s = String.format("%s << %d(%d,%d)", s, node.v, node.i, node.j);
			node = node.prev;
		}
		System.out.println(s);
	}
}
