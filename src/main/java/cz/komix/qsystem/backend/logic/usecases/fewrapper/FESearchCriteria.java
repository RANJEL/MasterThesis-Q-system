package cz.komix.qsystem.backend.logic.usecases.fewrapper;

import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.JaxbSearchCriteria;

import java.util.ArrayList;
import java.util.List;

/**
 * FE wrapper of {@link JaxbSearchCriteria} that adds {@link FEQueryModifier}s.
 *
 * @author Jan Lejnar
 */
public class FESearchCriteria extends JaxbSearchCriteria {
    private List<FEQueryModifier> queryModifiers;

    public FESearchCriteria(JaxbSearchCriteria jaxbSearchCriteria) {
        this.setName(jaxbSearchCriteria.getName());
        this.setElemOrder(jaxbSearchCriteria.getElemOrder());
        this.setFrontendElemType(jaxbSearchCriteria.getFrontendElemType());
        this.setFrontendValidations(jaxbSearchCriteria.getFrontendValidations());
        this.setAllowedQueryModifiers(jaxbSearchCriteria.getAllowedQueryModifiers());
        this.queryModifiers = new ArrayList<>();
    }

    public List<FEQueryModifier> getQueryModifiers() {
        return queryModifiers;
    }
}
