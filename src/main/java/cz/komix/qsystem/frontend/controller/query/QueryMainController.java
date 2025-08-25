package cz.komix.qsystem.frontend.controller.query;

import cz.komix.qsystem.backend.exception.ISAAAInterfaceNotFound;
import cz.komix.qsystem.backend.exception.ISAAARequestFailed;
import cz.komix.qsystem.frontend.controller.callService.AppFeaturesController;
import cz.komix.qsystem.frontend.controller.callService.MessagesDispatcherController;
import cz.komix.qsystem.frontend.controller.messages.MessagesController;
import cz.komix.qsystem.frontend.searchcriteria.SearchCriteriaGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

import static cz.komix.qsystem.frontend.controller.query.HitCandidatesSessionMapController.AAAIDToString;
import static cz.komix.qsystem.frontend.searchcriteria.categorygenerators.AbstractCategoryEntityFactory.PARAM_KEY_PREFIX;

/**
 * @author Jan Lejnar
 */
@Named
@ViewScoped
public class QueryMainController {

    private MessagesDispatcherController messagesDispatcherController;
    private AppFeaturesController appFeaturesController;
    private SearchCriteriaGenerator searchCriteriaGenerator;
    private final Logger logger = LoggerFactory.getLogger(QueryMainController.class);

    private String queryCategory;
    private String queryEntity;
    private StandardQueryType searchCriteria;

    private int hitCounts = 0;
    private String recordsFound = "";

    private HitCandidatesSessionMapController hitCandidatesSessionMapController;
    private StandardQueryResponseData standardQueryResponseData;
    private ISAAARequestFailed ISAAARequestFailedException;

    private MessagesController messagesController;

    @Inject
    public QueryMainController(
            MessagesDispatcherController messagesDispatcherController,
            SearchCriteriaGenerator searchCriteriaGenerator,
            HitCandidatesSessionMapController hitCandidatesSessionMapController,
            AppFeaturesController appFeaturesController,
            MessagesController messagesController) {
        this.messagesDispatcherController = messagesDispatcherController;
        this.searchCriteriaGenerator = searchCriteriaGenerator;
        this.hitCandidatesSessionMapController = hitCandidatesSessionMapController;
        this.appFeaturesController = appFeaturesController;
        this.messagesController = messagesController;
    }

    private void addHitCandidatesToSessionBean(StandardQueryHitListType hitCandidatesWrapper) {
        this.hitCounts = ((hitCandidatesWrapper.getHitCandidates() != null) ? hitCandidatesWrapper.getHitCandidates().getHitCandidate().size() : 0);

        for (int i = 0; i < this.hitCounts; i++) {
            StandardQueryHitListType.HitCandidates.HitCandidate hitCandidate =
                    hitCandidatesWrapper.getHitCandidates().getHitCandidate().get(i);
            hitCandidatesSessionMapController.putHitCandidate(hitCandidate);
        }
    }

    public String getRecordsFound() {
        return this.recordsFound;
    }

    /**
     * Sentence with count of found records.
     */
    private void setRecordsFound() {
        long responseHitCounts = standardQueryResponseData.getHitList().getHitCounts();
        this.recordsFound = messagesController.getResourceMessage("generic.label.recordsFound.nationalCopy") + ": " + responseHitCounts;
        if (responseHitCounts > this.hitCounts) {
            this.recordsFound += messagesController.getResourceMessage("generic.label.recordsFound.detailDisplayedFirst") + ": " + this.hitCounts;
        } else {
            this.recordsFound += messagesController.getResourceMessage("generic.label.recordsFound.detailDisplayedAll");
        }
    }

    public void dispatchStandardQuery() {
        Map<String, String> requestParamMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        queryCategory = requestParamMap.get("app-form:queryCategory");
        queryEntity = requestParamMap.get("app-form:queryEntity");

        searchCriteria = searchCriteriaGenerator.getSearchCriteria(requestParamMap, queryCategory, queryEntity);

        try {
            if (/* ANONYMIZED */) {
                standardQueryResponseData = messagesDispatcherController.dispatchStandardQuery(reason, searchCriteria);
                addHitCandidatesToSessionBean(standardQueryResponseData.getHitList());

                this.setRecordsFound();
            } else {
                messagesController.growlMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "generic.label.error",
                        "generic.message.enterReason"
                );
            }
        } catch (ISAAARequestFailed ex) {
            logger.error("Received response with error: {}", ex.getMessage(), ex);
            ISAAARequestFailedException = ex;
        } catch (ISAAAInterfaceNotFound ex) {
            logger.error("IS AAA interface not found, interface.toml is probably corrupted", ex);
        }
    }

    public void dispatchComplementQuery(AAAIDType AAAIDType) {
        try {
            if (/* ANONYMIZED */) {
                standardQueryResponseData = messagesDispatcherController.dispatchComplementQuery(reason, AAAIDType);
                addHitCandidatesToSessionBean(standardQueryResponseData.getHitList());

                this.setRecordsFound();
            } else {
                messagesController.growlMessage(
                        FacesMessage.SEVERITY_ERROR,
                        messagesController.getResourceMessage("generic.label.error"),
                        messagesController.getResourceMessage("generic.message.enterReason")
                );
            }
        } catch (ISAAARequestFailed ex) {
            logger.error("Received response with error: {}", ex.getMessage(), ex);
            ISAAARequestFailedException = ex;
        } catch (ISAAAInterfaceNotFound ex) {
            logger.error("IS AAA interface not found, interface.toml is probably corrupted", ex);
        }
    }

    public StandardQueryResponseData getStandardQueryResponseData() {
        return standardQueryResponseData;
    }

    public ISAAARequestFailed getISAAARequestFailedException() {
        return ISAAARequestFailedException;
    }

    public boolean wasReceivedValidResponse() {
        return standardQueryResponseData != null &&
                ISAAARequestFailedException == null;
    }

    public boolean wasReceivedInvalidResponse() {
        return standardQueryResponseData == null &&
                ISAAARequestFailedException != null;
    }

    public String viewCandidateDetail(cz.komix.isaaa.query.response.AAAIDType AAAID) {
        return "response-detail.xhtml?AAAID=" + AAAIDToString(AAAID);
    }
}
