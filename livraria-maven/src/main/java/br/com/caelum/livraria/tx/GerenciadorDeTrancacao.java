package br.com.caelum.livraria.tx;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

@SuppressWarnings("serial")
@Transacional
@Interceptor
public class GerenciadorDeTrancacao implements Serializable{
	
	@Inject
	EntityManager manager;
	
	@AroundInvoke
	public Object executaTX(InvocationContext contexto) throws Exception {
		
		// abre transacao
		System.out.println("abrindo tx");
		manager.getTransaction().begin();
		
		// chamar os DAOs que precisam de transação
		Object resultado = contexto.proceed();
		
		System.out.println("fechando tx");
		// commita a transacao
		manager.getTransaction().commit();
		
		return resultado;
	}

}
