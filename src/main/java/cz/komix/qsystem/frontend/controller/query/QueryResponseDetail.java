package cz.komix.qsystem.frontend.controller.query;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletRequest;

/**
 * @author Jan Lejnar
 */
@Named
@ViewScoped
public class QueryResponseDetail {

    private StandardQueryHitListType.HitCandidates.HitCandidate hitCandidate;

    @Inject
    public QueryResponseDetail(HitCandidatesSessionMapController hitCandidatesSessionMapController) {
        String AAAID = ((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .getParameter("AAAID");
        this.hitCandidate = hitCandidatesSessionMapController.getHitCandidate(AAAID);
    }

    public StandardQueryHitListType.HitCandidates.HitCandidate getHitCandidate() {
        return hitCandidate;
    }
}
