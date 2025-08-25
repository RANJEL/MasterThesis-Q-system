package cz.komix.qsystem.backend.logic.isaaa;

import org.springframework.beans.factory.annotation.Value;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceRef;

/**
 * Proxy bean to make sure that ISAAAQueryService Stub will be unique for each request.
 * JAX-WS client call is not thread safe otherwise!
 *
 * @author Jan Lejnar
 */
@Named
@RequestScoped
public class ISAAAQueryServicesProxy {

	@Value("${query.service.url}")
	private String queryUrl;

	@Value("${extquery.service.url}")
	private String extqueryUrl;

	@WebServiceRef(value = ISAAAQueryService.class)
	private ISAAAQueryService iSAAAQueryService;

	@WebServiceRef(value = ISAAAQuerySimplifiedService.class)
	private ISAAAQuerySimplifiedService iSAAAQuerySimplifiedService;

	public ISAAAQueryService getISAAAQueryService() {
		((BindingProvider) iSAAAQueryService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				this.queryUrl);
		return iSAAAQueryService;
	}

	public ISAAAQuerySimplifiedService getISAAAQuerySimplifiedService() {
		((BindingProvider) iSAAAQuerySimplifiedService).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				this.extqueryUrl);
		return iSAAAQuerySimplifiedService;
	}
}
