package cz.komix.qsystem.frontend.controller.admin.queryForms;

import cz.komix.qsystem.backend.logic.queryform.QueryForm;
import cz.komix.qsystem.backend.logic.queryform.element.ElementaryQueryForm;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QFVertexCategoriesEnum;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryEntityType;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryModifier;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryType;
import cz.komix.qsystem.backend.logic.usecases.fewrapper.FEQueryEntityType;
import cz.komix.qsystem.frontend.controller.callService.AppFeaturesController;
import cz.komix.qsystem.frontend.controller.callService.AuthorizationController;
import cz.komix.qsystem.frontend.controller.messages.MessagesController;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletRequest;
import java.util.*;

import static cz.komix.qsystem.backend.logic.usecases.QSystemUseCasesImpl.*;
import static cz.komix.qsystem.frontend.searchcriteria.categorygenerators.AbstractCategoryEntityFactory.isCheckedCheckbox;
import static cz.komix.qsystem.frontend.searchcriteria.categorygenerators.AbstractCategoryEntityFactory.isValidParamValue;

/**
 * @author Jan Lejnar
 */
@Named
@ViewScoped
public class QueryFormsDetailController {

    public static final String PARAM_KEY_PREFIX = "app-form:query-form-detail-form:";
    Map<String, String> requestParamMap;
    private QueryForm queryFormBeforeModification;
    private AuthorizationController authorizationController;
    private AppFeaturesController appFeaturesController;
    private MessagesController messagesController;
    /* hack to force jsf to believe that I here really contain values of checkbox fields */
    private boolean shouldCheckQueryType;
    private boolean shouldCheckQueryEntityType;
    private boolean shouldCheckQueryModifier;

    @Inject
    public QueryFormsDetailController(AuthorizationController authorizationController, AppFeaturesController appFeaturesController, MessagesController messagesController) {
        this.authorizationController = authorizationController;
        this.appFeaturesController = appFeaturesController;
        this.messagesController = messagesController;
        String queryFormId = ((ServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .getParameter("query_form_id");
        this.queryFormBeforeModification = authorizationController.getQueryForm(queryFormId);
        if (this.queryFormBeforeModification == null) {
            this.queryFormBeforeModification = new QueryForm("");
        }
    }

    public QueryForm getQueryFormBeforeModification() {
        return queryFormBeforeModification;
    }

    public boolean shouldCheckQueryType(String queryTypeName) {
        return queryFormBeforeModification.getQueryTypeVertices().contains(new QueryType(queryTypeName));
    }

    public boolean shouldCheckQueryEntityType(String queryEntityTypeCategory, String queryEntityType) {
        return queryFormBeforeModification.getQueryEntityTypeVertices().contains(new QueryEntityType(queryEntityTypeCategory + ":" + queryEntityType));
    }

    public boolean shouldCheckQueryModifier(String queryEntityTypeCategory, String queryEntityType, String queryModifierName) {
        QueryEntityType startingVertex = new QueryEntityType(queryEntityTypeCategory + ":" + queryEntityType);
        return queryFormBeforeModification.getConnectedVerticesOfSpecificTypeWithConnectionToSpecificVertex(
                startingVertex,
                QFVertexCategoriesEnum.QUERY_MODIFIER,
                stdQueryVertex
        ).contains(new QueryModifier(queryModifierName));
    }

    public boolean isShouldCheckQueryType() {
        return shouldCheckQueryType;
    }

    public void setShouldCheckQueryType(boolean shouldCheckQueryType) {
        this.shouldCheckQueryType = shouldCheckQueryType;
    }

    public boolean isShouldCheckQueryEntityType() {
        return shouldCheckQueryEntityType;
    }

    public void setShouldCheckQueryEntityType(boolean shouldCheckQueryEntityType) {
        this.shouldCheckQueryEntityType = shouldCheckQueryEntityType;
    }

    public boolean isShouldCheckQueryModifier() {
        return shouldCheckQueryModifier;
    }

    public void setShouldCheckQueryModifier(boolean shouldCheckQueryModifier) {
        this.shouldCheckQueryModifier = shouldCheckQueryModifier;
    }

    /**/

    private boolean formContainsCheckedCheckbox(String checkboxName) {
        String paramKey = QueryFormsDetailController.PARAM_KEY_PREFIX + checkboxName + "_input";
        String checkboxValue = requestParamMap.get(paramKey);
        return isCheckedCheckbox(checkboxValue);
    }

    private void fillQueryTypes(Set<QueryType> checkedQueryTypes) {
        appFeaturesController.getAllQueryTypes().forEach(
                queryType -> {
                    if (formContainsCheckedCheckbox(queryType.getName())) {
                        checkedQueryTypes.add(new QueryType(queryType.getName()));
                    }
                }
        );
    }

    private void fillQueryEntityTypesAndModifiers(
            Map<QueryEntityType, List<QueryModifier>> checkedModifiersForQueryEntityType,
            List<FEQueryEntityType> entityTypesToGoThrough,
            String entityTypesCategory
    ) {
        entityTypesToGoThrough.forEach(
                queryEntity -> {
                    String formKey = entityTypesCategory + "_" + queryEntity.getName();
                    if (formContainsCheckedCheckbox(formKey)) {
                        QueryEntityType queryEntityType = new QueryEntityType(entityTypesCategory + ":" + queryEntity.getName());
                        checkedModifiersForQueryEntityType.put(queryEntityType, new ArrayList<>());

                        appFeaturesController.getAllQueryModifiers().forEach(
                                queryModifier -> {
                                    if (formContainsCheckedCheckbox(formKey + "-" + queryModifier.getName())) {
                                        checkedModifiersForQueryEntityType.get(queryEntityType).add(new QueryModifier(queryModifier.getName()));
                                    }
                                }
                        );
                    }
                }
        );
    }

    private void fillQueryFormWithDataFromForm(QueryForm queryForm) {
        requestParamMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        Set<QueryType> checkedQueryTypes = new HashSet<>();
        Map<QueryEntityType, List<QueryModifier>> checkedModifiersForQueryEntityType = new HashMap<>();

        fillQueryTypes(checkedQueryTypes);
        fillQueryEntityTypesAndModifiers(
                checkedModifiersForQueryEntityType,
                appFeaturesController.getAllSingleCategoryQueryEntities(),
                "single_category"
        );
        fillQueryEntityTypesAndModifiers(
                checkedModifiersForQueryEntityType,
                appFeaturesController.getAllMultiCategoryQueryEntities(),
                "multi_category"
        );

        // special representation
        checkedQueryTypes.forEach(
                queryType -> {
                    if (!queryType.equals(stdQueryVertex)) {
                        queryForm.addElementaryQueryForm(new ElementaryQueryForm(
                                anyEntityTypeVertex,
                                anyModifierVertex,
                                queryType
                        ));
                    }
                }
        );

        checkedModifiersForQueryEntityType.forEach(
                ((queryEntityType, queryModifiers) -> {
                    // need to add queryEntityType vertex when queryModifiers list is empty
                    queryForm.addElementaryQueryForm(new ElementaryQueryForm(
                            queryEntityType,
                            exactModifierVertex,
                            stdQueryVertex
                    ));
                    queryModifiers.forEach(
                            queryModifier -> queryForm.addElementaryQueryForm(new ElementaryQueryForm(
                                    queryEntityType,
                                    queryModifier,
                                    stdQueryVertex
                            ))

                    );
                })
        );
    }

    public String saveQueryForm() {
        QueryForm queryForm = new QueryForm(queryFormBeforeModification.getName(), queryFormBeforeModification.getDescription());
        fillQueryFormWithDataFromForm(queryForm);
        if (isValidParamValue(queryForm.getName())) {
            authorizationController.updateQueryForm(queryForm);
        }
        return "main.xhtml?faces-redirect=true";
    }
}
