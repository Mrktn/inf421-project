import java.util.*;
public class dancingLinks<E extends Comparable<E>> extends dtNode {
	
	int size; //nombre de colones (éléments différents dans l'ensemble de base)
	
	@SuppressWarnings("unchecked")
	dancingLinks(Vector<E> S, Vector<Vector<E>> C){ //création à partir d'une liste de nombres et d'un ensemble d'ensembles (type vector pas forcément idéal -- on peut en discuter)
		
		super(null,null,null,null,null);
		
		this.L = this;
		this.R = this;
		
		Collections.sort(S); //L'un des ajouts que j'ai fait par rapport au cas "de base" consiste à trier les elements, pour que l'ajout des elements se fasse plus rapidement. Il faut pour cela un type générique qui soir comparable
		this.size = S.size();
		
		if(S.isEmpty()){
			return;
		}
		
		columnNode<E> buffer;
		
		buffer = new columnNode<E>(this, S.get(0), this);
		
		for(int i = 1; i < S.size(); i++){
			buffer = new columnNode<E>(buffer, S.get(i), this); //ajout des colonnes
		}
		
		dataNode<E> buffer2 = null;
		
		for(Vector<E> T : C){ //ajout des ensembles dans la structure
			
			Collections.sort(T); //encore une fois, le tri permet un ajout plus rapide
			
			buffer = (columnNode<E>) this.R;
			
			int i = 0;
			
			buffer2 = null;
			
			while(i < T.size())
			{
				
				if(T.get(i).equals(buffer.item)){
					buffer2 = new dataNode<E>(buffer2, buffer, T);
					i++;
				}
				if(buffer.R == this)
					break;
				buffer = (columnNode<E>) buffer.R;
				
			}
		}
		
	}
	
	public void addRNode(dtNode that){ //ajout d'une colonne. Fonctionnalité non utilisée, car les colonnes sont déjà ajoutées dans le constructeur
		
		this.size++;
		
		if(this.size == 1){
			
			
			that.L = this;
			that.R = this;
			this.R = that;
			this.L = that;
			
			return;
		}
		
		this.R.addRNode(that);
		
	}
	
	/*
	 * Le code suivant permet la représentation graphique sous forme matricielle de la structure. Elle a été implémenté a fin de verifier la correction de l'algorithme
	 */
	
	
	/*

	int height;
	
	@SuppressWarnings("unchecked")
	dancingLinks(Vector<E> S, Vector<Vector<E>> C){
		
		super(null,null,null,null,null);
		
		this.L = this;
		this.R = this;
		
		Collections.sort(S);
		this.size = S.size();
		
		if(S.isEmpty()){
			return;
		}
		
		columnNode<E> buffer;
		
		buffer = new columnNode<E>(this, S.get(0), this);
		
		for(int i = 1; i < S.size(); i++){
			buffer = new columnNode<E>(buffer, S.get(i), this);
		}
		
		dataNode<E> buffer2 = null;
		
		int j = 0;
		
		for(Vector<E> T : C){
			
			Collections.sort(T);
			
			buffer = (columnNode<E>) this.R;
			
			int i = 0;
			
			buffer2 = null;
			
			while(i < T.size())
			{
				
				if(T.get(i).equals(buffer.item)){
					buffer2 = new dataNode<E>(buffer2, buffer, T, j);
					i++;
				}
				if(buffer.R == this)
					break;
				buffer = (columnNode<E>) buffer.R;
				
			}
			
			j++;
		}
		
		this.height = C.size();
		
	}
	
	@SuppressWarnings("unchecked")
	public String toString(){
		if(this.R == this)
			return "void";
		
		columnNode<E> buffer = (columnNode<E>) this.R;
		
		String str = "[";
		
		while(true){
			str += buffer.toString() + ";";
			if(buffer.R == this){
				break;
			}
			
			buffer = (columnNode<E>) buffer.R;
		}
		str += "]";
		
		return str;
	}
	
	
	public String visualRPZ(){
		
		if(this.R == this){
			return "void";
		}
		
		int[][] matrix = new int[this.height][this.size];
		
		dtNode buffer = this.R;
		
		for(int i = 0; i < this.size; i++){
			
			buffer.visualRPZ(i,matrix);
			buffer = buffer.R;
			
		}
		
		String str = "";
		
		for(int[] e : matrix){
			for(int f : e){
				str += f + " ";
			}
			str += "\n";
		}
		
		return str;
	}
	
	public void visualRPZ(int index, int[][] matrix){
		
		return;
		
	}*/
	
}
