package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.livraria.dao.AutorDao;
import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.dao.LivroDao;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.tx.Transacional;

@Named
@ViewScoped
public class LivroBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Livro livro = new Livro();
	private Integer autorId;
	private Integer livroId;
	private List<Livro> livros;	
	
	@Inject
	LivroDao livroDao;
	
	@Inject
	AutorDao autorDao;
	
	@Inject
	FacesContext context;
	
	public Integer getLivroId() {
		return livroId;
	}

	public void setLivroId(Integer livroId) {
		this.livroId = livroId;
	}

	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public Livro getLivro() {
		return livro;
	}
	
	public List<Livro> getLivros() {
		if(livros == null) {
			this.livros = livroDao.listaTodos();
		}
		return livros;
	}
	
	public List<Autor> getAutores(){
		return autorDao.listaTodos();
	}
	
	public List<Autor> getAutoresDoLivro(){
		return this.livro.getAutores();
	}
	
	public void carregaPelaId() {
		this.livro = livroDao.buscaPorId(this.livroId);
	}

	public void gravarAutor() {
		Autor autor = autorDao.buscaPorId(this.autorId);
		this.livro.adicionaAutor(autor);
		System.out.println("Livro escrito por: " + autor.getNome());
	}
	
	@Transacional
	public void gravar() {
		System.out.println("Gravando livro " + this.livro.getTitulo());

		if (livro.getAutores().isEmpty()) {
			//throw new RuntimeException("Livro deve ter pelo menos um Autor.");
			context.addMessage("autor", new FacesMessage("Livro deve ter pelo menos um Autor"));
			return;
		}
			
		if(this.livro.getId() == null) {
			livroDao.adiciona(this.livro);
			this.livros = livroDao.listaTodos();
		} else {
			livroDao.atualiza(this.livro);
		}
		
		this.livro = new Livro();
	}
	
	@Transacional
	public void remover(Livro livro) {
		System.out.println("Removendo livro");
		
		this.livroDao.remove(livro);
		this.livros = this.livroDao.listaTodos();
	}
	
	public void removerAutorDoLivro(Autor autor) {
		this.livro.removeAutor(autor);
		
	}
	
	public void carregar(Livro livro) {
		System.out.println("Carregando livro");
		this.livro = this.livroDao.buscaPorId(livro.getId());
	}

	public String formAutor() {
		System.out.println("Chamando o formul�rio do Autor");
		return "autor?faces-redirect=true";		
	}
	
	public void comecaComDigitoUm(FacesContext fc, UIComponent component, Object value) throws ValidatorException {
		String valor = value.toString();
		
		if(!valor.startsWith("1")) {
			throw new ValidatorException(new FacesMessage("ISBN deveria come�ar com 1"));
		}
		
	}
}
