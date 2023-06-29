package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	ImdbDAO dao;
	
	private Graph<Director, DefaultWeightedEdge> graph;

	public Model() {
		this.dao = new ImdbDAO();
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}
	
	public Graph creaGrafo(int anno) {
		
		// creo il grafo nuovo tutte le volte
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
				
		Graphs.addAllVertices(this.graph, this.dao.directorsForYear(anno));
		
		//List<Archi> archi = new ArrayList<>();
		
		
		for(Director d1: this.graph.vertexSet()) {
			for(Director d2 : this.graph.vertexSet()) {
				if(d1.getId() != d2.getId()) {
					//int peso = 0;
					
					List<Integer> attoriUguali = new ArrayList<>();
					
					List<Actor> attori1 = this.dao.getActorsByDirectorId(anno,d1.getId());
					List<Actor> attori2 = this.dao.getActorsByDirectorId(anno,d2.getId());
					
					/*for(Actor a1 : attori1) {
						for(Actor a2 : attori2) {
							if(a1.getId() == a2.getId()) {
								//peso++;
								attoriUguali.add(a1.getId());
							}
						}
					}*/
					for(Actor a1: attori1) {
						if(attori2.contains(a1)) {
							attoriUguali.add(a1.getId());
						}
					}
					
					if(attoriUguali.size()!=0) {
						Graphs.addEdge(this.graph, d1,d2, attoriUguali.size());
					}
					//archi.add(new Archi(d1,d2,peso));
				}
			}
		}
		
		/*for(Archi a : archi) {
			if(a.getPeso()>0) {
				Graphs.addEdge(this.graph, a.getD1(), a.getD2(), a.getPeso());
			}
		}*/
		
		
		return graph;
	}
	
	public List<Director> getAdiacenti(Director director){
		ConnectivityInspector<Director, DefaultWeightedEdge> inspector = new ConnectivityInspector<>(this.graph);
		Set<Director> setConnesso = inspector.connectedSetOf(director);
		List<Director> listaConnessi = new ArrayList<>(setConnesso);
		
		return listaConnessi;
	}
	

}
