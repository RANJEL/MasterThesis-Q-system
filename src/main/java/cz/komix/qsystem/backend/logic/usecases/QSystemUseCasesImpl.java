package cz.komix.qsystem.backend.logic.usecases;

import cz.komix.qsystem.backend.exception.ISAAAInterfaceNotFound;
import cz.komix.qsystem.backend.exception.ISAAARequestFailed;
import cz.komix.qsystem.backend.logic.isaaa.AbstractISAAAAppInterface;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.*;
import cz.komix.qsystem.backend.logic.usecases.fewrapper.FEQueryEntityType;
import cz.komix.qsystem.backend.logic.usecases.fewrapper.FEQueryModifier;
import cz.komix.qsystem.backend.logic.usecases.fewrapper.FESearchCriteria;
import cz.komix.qsystem.backend.persistence.appfeatures.IAppFeatures;
import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.JaxbQueryEntityType;
import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.JaxbQueryModifier;
import cz.komix.qsystem.backend.persistence.appfeatures.jaxb.JaxbSearchCriteria;
import cz.komix.qsystem.backend.persistence.config.AbstractConfigurationProvider;
import cz.komix.qsystem.backend.persistence.config.part.interfaces.IInterfacesConfig;
import cz.komix.qsystem.backend.security.user.LoggedUser;
import cz.komix.qsystem.backend.security.user.LoggedUserProvider;
import cz.komix.qsystem.frontend.searchcriteria.categorygenerators.multi.factory.AbstractMultiCategoryEntityFactory;
import cz.komix.qsystem.frontend.searchcriteria.categorygenerators.single.factory.AbstractSingleCategoryEntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

/**
 * @author Jan Lejnar
 */
@Named
public class QSystemUseCasesImpl implements IQSystemUseCases {

    public static QueryType stdQueryVertex = new QueryType("standard_query");
    public static QueryModifier exactModifierVertex = new QueryModifier("exact");
    public static QueryModifier anyModifierVertex = new QueryModifier("ALL");
    public static QueryEntityType anyEntityTypeVertex = new QueryEntityType("ALL");
    private final Logger logger = LoggerFactory.getLogger(QSystemUseCasesImpl.class);
    private IInterfacesConfig interfacesConfig;
    private IAppFeatures appFeatures;
    private SortedSet<JaxbQueryModifier> allQueryModifiers;
    @Inject
    private Provider<LoggedUserProvider> loggedUserProvider;

    @Inject
    public QSystemUseCasesImpl(@Named("tomlConfigurationProvider") AbstractConfigurationProvider configuration, IAppFeatures appFeatures) {
        this.interfacesConfig = configuration.getInterfacesConfiguration();
        this.appFeatures = appFeatures;
        this.allQueryModifiers = appFeatures.getAllQueryModifiers();
    }

    @Override
    public StandardQueryResponseData dispatchStandardQuery(String reason, StandardQueryType searchCriteria) throws ISAAARequestFailed, ISAAAInterfaceNotFound {
        StandardQueryResponseData standardQueryResponseData;
        try {
            standardQueryResponseData = interfacesConfig.getCurrentlyUsedISAAAInterface().dispatchStandardQuery(reason, searchCriteria);
        } catch (ISAAARequestFailed ex) {
            if (ex.getMessage().contains("javax.net.ssl.SSLHandshakeException: Received fatal record: bad_certificate")) {
                // UC PP0102 step 6a1
                if (interfacesConfig.getCurrentlyUsedISAAAInterface().equals(interfacesConfig.getFullInterface())) {
                    interfacesConfig.setCurrentlyUsedISAAAInterface(interfacesConfig.getSimplifiedInterface());
                    standardQueryResponseData = interfacesConfig.getCurrentlyUsedISAAAInterface().dispatchStandardQuery(reason, searchCriteria);
                } else {
                    throw ex;
                }
            } else {
                throw ex;
            }
        }
        return standardQueryResponseData;
    }

    @Override
    public StandardQueryResponseData dispatchComplementQuery(String reason, AAAIDType AAAID) throws ISAAARequestFailed, ISAAAInterfaceNotFound {
        return interfacesConfig.getCurrentlyUsedISAAAInterface().dispatchComplementQuery(reason, AAAID);
    }

    @Override
    public String dispatchLoadAttachments(String reason, AAAIDType AAAID, long dataId) throws ISAAARequestFailed, ISAAAInterfaceNotFound {
        return interfacesConfig.getCurrentlyUsedISAAAInterface().dispatchLoadAttachments(reason, AAAID, dataId);
    }

    @Override
    public String dispatchLoadLinks(String reason, AAAIDType AAAID, long dataId) throws ISAAARequestFailed, ISAAAInterfaceNotFound {
        return interfacesConfig.getCurrentlyUsedISAAAInterface().dispatchLoadLinks(reason, AAAID, dataId);
    }

    @Override
    public void logEntryIntoAdministration(String userName, String adminBookmark) {
        Logger logger = LoggerFactory.getLogger(AbstractConfigurationProvider.class);
        logger.info("User {} entered Q-system administration, bookmark {}", userName, adminBookmark);
    }

    @Override
    public void setPreferredISAAAInterface(String isAAAInterfaceName) {
        try {
            AbstractISAAAAppInterface interfaceToSet = interfacesConfig.getISAAAAppInterfaces().get(isAAAInterfaceName);
            synchronized (this) {
                interfacesConfig.setPreferredISAAAInterface(interfaceToSet);
                interfacesConfig.setCurrentlyUsedISAAAInterface(interfaceToSet);
            }
        } catch (ISAAAInterfaceNotFound ISAAAInterfaceNotFound) {
            logger.error("Could not find IS AAA interface {}", isAAAInterfaceName, ISAAAInterfaceNotFound);
        }
    }

    private List<FESearchCriteria> wrapSearchCriteria(List<JaxbSearchCriteria> searchCriteria) {
        List<FESearchCriteria> feSearchCriteriaList = new ArrayList<>();

        searchCriteria.forEach(s -> {
            FESearchCriteria feSearchCriteria = new FESearchCriteria(s);

            allQueryModifiers.forEach(jaxbQueryModifier ->
                    feSearchCriteria.getQueryModifiers().add(new FEQueryModifier(jaxbQueryModifier)));


            feSearchCriteriaList.add(feSearchCriteria);
        });

        return feSearchCriteriaList;
    }

    private void disableModifiersNotAllowedBasedOnICT(List<FESearchCriteria> feSearchCriteriaList) {
        feSearchCriteriaList.forEach(
                feSearchCriteria -> feSearchCriteria.getQueryModifiers().forEach(
                        feQueryModifier -> {
                            JaxbQueryModifier investigatedQueryModifier = new JaxbQueryModifier();
                            investigatedQueryModifier.setName(feQueryModifier.getName());
                            if (!feSearchCriteria.getAllowedQueryModifiers().contains(investigatedQueryModifier)) {
                                feQueryModifier.setCheckable(false);
                            }
                        })
        );
    }

    private void disableModifiersNotAllowedBasedOnLoggedUser(List<FESearchCriteria> feSearchCriteriaList, String queryEntityTypeCategory, String queryEntityType) {
        LoggedUser loggedUser = getLoggedUser();

        QFVertex startingVertex = transformIntoQueryEntityRepresentation(queryEntityTypeCategory, queryEntityType);
        Set<QFVertex> checkableModifiers =
                loggedUser.getLoggedUserQueryFormIntersection().getConnectedVerticesOfSpecificTypeWithConnectionToSpecificVertex(
                        startingVertex,
                        QFVertexCategoriesEnum.QUERY_MODIFIER,
                        stdQueryVertex);

        feSearchCriteriaList.forEach(
                feSearchCriteria -> feSearchCriteria.getQueryModifiers().forEach(
                        feQueryModifier -> {
                            if (!checkableModifiers.contains(new QueryModifier(feQueryModifier.getName()))) {
                                feQueryModifier.setCheckable(false);
                            }
                        }
                )
        );
    }

    @Override
    public List<FESearchCriteria> getAllSearchCriteriaWithCheckedInfo(String queryEntityTypeCategory, String queryEntityType) {
        List<JaxbSearchCriteria> searchCriteria =
                appFeatures.sortSearchCriteriaByOrder(
                        appFeatures.getAllSearchCriteria(queryEntityTypeCategory, queryEntityType));

        List<FESearchCriteria> feSearchCriteriaList = this.wrapSearchCriteria(searchCriteria);
        disableModifiersNotAllowedBasedOnICT(feSearchCriteriaList);
        disableModifiersNotAllowedBasedOnLoggedUser(feSearchCriteriaList, queryEntityTypeCategory, queryEntityType);
        return feSearchCriteriaList;
    }

    private List<FEQueryEntityType> wrapQueryEntityTypes(List<JaxbQueryEntityType> queryEntityTypes) {
        List<FEQueryEntityType> feQueryEntityTypes = new ArrayList<>();

        queryEntityTypes.forEach(entityType -> {
            FEQueryEntityType feQueryEntityType = new FEQueryEntityType(entityType);
            feQueryEntityTypes.add(feQueryEntityType);
        });

        return feQueryEntityTypes;
    }

    private QueryEntityType transformIntoQueryEntityRepresentation(String queryEntityTypeCategory, String queryEntityType) {
        if (queryEntityTypeCategory == null ||
                queryEntityType == null ||
                queryEntityTypeCategory.trim().equals("") ||
                queryEntityType.trim().equals("")) {
            throw new IllegalArgumentException("Can't create QueryEntityType based on provided parameters: " +
                    "queryEntityTypeCategory=" + queryEntityTypeCategory + ", queryEntityType=" + queryEntityType);
        }
        return new QueryEntityType(queryEntityTypeCategory + ':' + queryEntityType);
    }

    private void disableQueryEntitiesNotAllowedBasedOnLoggedUser(String queryEntityTypeCategory, List<FEQueryEntityType> feQueryEntityTypes) {
        LoggedUser loggedUser = getLoggedUser();
        Set<QueryEntityType> loggedUserAllowedQueryEntities =
                loggedUser.getLoggedUserQueryFormIntersection().getConnectedVerticesOfSpecificType(stdQueryVertex, QFVertexCategoriesEnum.QUERY_ENTITY_TYPE)
                        .stream().map(qfVertex -> (QueryEntityType) qfVertex).collect(Collectors.toSet());
        feQueryEntityTypes.forEach(
                feQueryEntityType -> {
                    QueryEntityType queryEntity = transformIntoQueryEntityRepresentation(
                            queryEntityTypeCategory,
                            feQueryEntityType.getName()
                    );
                    feQueryEntityType.setSelectable(loggedUserAllowedQueryEntities.contains(queryEntity));
                }
        );
    }

    private List<FEQueryEntityType> processQueryEntities(String queryEntityTypeCategory, List<JaxbQueryEntityType> queryEntityTypes) {
        List<FEQueryEntityType> feQueryEntityTypes = wrapQueryEntityTypes(queryEntityTypes);
        disableQueryEntitiesNotAllowedBasedOnLoggedUser(queryEntityTypeCategory, feQueryEntityTypes);
        return feQueryEntityTypes;
    }

    @Override
    public List<FEQueryEntityType> getAllSingleCategoryQueryEntities() {
        return processQueryEntities(
                AbstractSingleCategoryEntityFactory.CATEGORY_NAME,
                appFeatures.sortQueryEntitiesByOrder(
                        appFeatures.getAllSingleCategoryQueryEntities()
                ));
    }

    @Override
    public List<FEQueryEntityType> getAllMultiCategoryQueryEntities() {
        return processQueryEntities(
                AbstractMultiCategoryEntityFactory.CATEGORY_NAME,
                appFeatures.sortQueryEntitiesByOrder(
                        appFeatures.getAllMultiCategoryQueryEntities()
                ));
    }

    @Override
    public LoggedUser getLoggedUser() {
        return loggedUserProvider.get().getLoggedUser();
    }
}
