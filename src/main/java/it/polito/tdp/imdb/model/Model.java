package it.polito.tdp.imdb.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	ImdbDAO dao;
	private SimpleWeightedGraph<Director, DefaultWeightedEdge> grafo;
	HashMap<Integer, Director> idMap;
	
	public Model() {
		dao= new ImdbDAO();
	}
	
	public void creaGrafo(int year) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap= new HashMap<Integer, Director>();
		dao.getVertices(idMap, year);
		Graphs.addAllVertices(grafo, idMap.values());
		System.out.println(grafo.vertexSet().size());
		LinkedList<Arco> archi= new LinkedList<>();
		
		archi=dao.getArchi(idMap,year);
		for(Arco a: archi)
		{
			if(grafo.containsVertex(a.d1)&&grafo.containsVertex(a.d2))
			{
				Graphs.addEdge(grafo,a.d1, a.d2, a.peso);
			}
		}
		System.out.println(grafo.edgeSet().size());
	}
	public LinkedList<Director> listaRegisti()
	{
		return new LinkedList<Director>(grafo.vertexSet());
	}
	public LinkedList<Director> adiacenti(Director d){
		LinkedList<Director> directors= new LinkedList<>(Graphs.neighborListOf(grafo, d));
		Collections.sort(directors,( d1, d2)-> ((int)(-grafo.getEdgeWeight(grafo.getEdge(d, d1))+(grafo.getEdgeWeight(grafo.getEdge(d, d2))))));
		return directors;
	}
	LinkedList<Director> result= new LinkedList<>();
	int costomigliore;
	LinkedList<Director> davisitare;
	public LinkedList<Director> percorso(Director d,int c) {
		result= new LinkedList<Director>();
		LinkedList<Director> parziale= new LinkedList<>();
		parziale.add(d);
		
		davisitare=new LinkedList<>(grafo.vertexSet());
		davisitare.remove(d);
		calcola(parziale,0,c);
		return result;
	}
	
	public void calcola(LinkedList<Director> parziale,int peso, int c)
	{
		if(peso>c)
		{
			return;
		}
		else if(peso==c)
		{
			if(parziale.size()>result.size()||result.size()==0)
			{
				result=new LinkedList<Director>(parziale);
				costomigliore=peso;
				
			}
			return;

		}
		else {
			if(parziale.size()>result.size()||result.size()==0)
			{
				result=new LinkedList<Director>(parziale);
				costomigliore=peso;
				
			}
		}
		for(Director d : Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) 
		{
			int nuovopeso=(int)grafo.getEdgeWeight(grafo.getEdge(parziale.get(parziale.size()-1), d));
			System.out.println("Nuovo Peso: "+nuovopeso);
			if(!parziale.contains(d))
			{
			parziale.add(d);
			System.out.println("Peso: "+peso);
			
			
			calcola(parziale,peso+nuovopeso,c);
			parziale.remove(d);
			}
		}
		
	}

}
