package cz.komix.qsystem.frontend.controller.callService;

import cz.komix.qsystem.backend.service.ISAAAInterfacesService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author Jan Lejnar
 */
@Named
@ApplicationScoped
public class ISAAAInterfacesController {

    private ISAAAInterfacesService ISAAAInterfacesService;

    @Inject
    public ISAAAInterfacesController(ISAAAInterfacesService ISAAAInterfacesService) {
        this.ISAAAInterfacesService = ISAAAInterfacesService;
    }

    public List<String> getISAAAInterfaceNames() {
        return ISAAAInterfacesService.getISAAAInterfaceNames();
    }

    public String getCurrentlyUsedISAAAInterface() {
        return ISAAAInterfacesService.getCurrentlyUsedISAAAInterface();
    }

    public String getPreferredISAAAInterface() {
        return ISAAAInterfacesService.getPreferredISAAAInterface();
    }

    public void setPreferredISAAAInterface(String isAAAInterfaceName) {
        ISAAAInterfacesService.setPreferredISAAAInterface(isAAAInterfaceName);
    }
}
