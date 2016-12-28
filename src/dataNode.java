import java.util.List;

public class dataNode<F extends List<E>, E extends Comparable<E>> extends dtNode {


	/*
	 * pour implementation avec les polyominos : size représente le nombre
	 * d'élements de la classe d'équivalence (fixed, free ou one-sided en fx de
	 * la situation). Il faudra donc prendre un représentant de chaque, classe
	 * d'équivalence, l'ajouter au dancinglinks, puis génered ses elements
	 * équivalents, et les ajouter en les linkant au premier de manière
	 * circulaire.
	 */

	dataNode(lineNode<F,E> Ln, dtNode C) {
		super(null, null, null, null, C, Ln);
		
		if (C != null) {
			C.addDNode(this);
		}

		if(Ln != null){
			Ln.addRNode(this);
		}

	}

	public void addDNode(dtNode that) {
		if (this.C == this.D) {
			this.D = that;
			that.U = this;

			that.C = this.C;

			that.D = this.C;
			this.C.U = that;

			return;
		}

		this.D.addDNode(that);

	}

	public void addRNode(dtNode that) {

		that.L = this;
		that.R = this.R;

		this.R.L = that;
		this.R = that;

	}
}