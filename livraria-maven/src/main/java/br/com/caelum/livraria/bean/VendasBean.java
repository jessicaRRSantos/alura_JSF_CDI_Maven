package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import br.com.caelum.livraria.dao.LivroDao;
import br.com.caelum.livraria.modelo.Venda;

@Named
@ViewScoped
public class VendasBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	LivroDao livroDao;
	
	@Inject
	EntityManager manager;
	
	public BarChartModel getVendasModel(){
		BarChartModel model = new BarChartModel();
		model.setAnimate(true);
		 
        ChartSeries vendaSerie = new ChartSeries();
        vendaSerie.setLabel("Vendas 2020");
        
        List<Venda> vendas = getVendas();        
        for (Venda venda : vendas) {
			vendaSerie.set(venda.getLivro().getTitulo(), venda.getQuantidade());
		}
              
        model.addSeries(vendaSerie);
 
        return model;
	}
	
	public List<Venda> getVendas(){
		
		/*List<Livro> livros = livroDao.listaTodos();
		List<Venda> vendas = new ArrayList<Venda>();
		
		Random random = new Random(seed);
		for (Livro livro : livros) {					
			Integer quantidade = random.nextInt(500);
			
			vendas.add(new Venda(livro, quantidade));		
		}*/
		
		List<Venda> vendas = this.manager.createQuery("select v from Venda v", Venda.class).getResultList();
		
		 return vendas;
	}
}
