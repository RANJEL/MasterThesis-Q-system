package cz.komix.qsystem.backend.persistence.config.part.translationdials.searchmode;

import cz.komix.qsystem.backend.exception.SearchModeNotFound;
import cz.komix.qsystem.backend.logic.queryform.representation.graph.vertices.QueryModifier;
import cz.komix.qsystem.backend.logic.translationdials.TranslationDialsTables;
import cz.komix.qsystem.backend.logic.translationdials.entry.CatalogEntry;
import cz.komix.qsystem.backend.persistence.config.AbstractConfigurationProvider;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jan Lejnar
 */
@Named
public class QueryModifiers2SearchModeConvertor {

    private final List<CatalogEntry> tableICT108_KombinaceModifikatoru;

    @Inject
    public QueryModifiers2SearchModeConvertor(@Named("tomlConfigurationProvider") AbstractConfigurationProvider configuration) {
        this.tableICT108_KombinaceModifikatoru = configuration.getTranslationDialsConfiguration().getTranslationDials().getEntriesFrom(TranslationDialsTables.KombinaceModifikatoru);
    }

    private boolean equalsQueryModifiersAndSearchMode(String[] searchModeQueryModifiers, Set<QueryModifier> queryModifiers) {
        if (searchModeQueryModifiers.length != queryModifiers.size()) {
            return false;
        }

        Set<String> queryModifierNames = new HashSet<>();
        queryModifiers.forEach(queryModifier -> queryModifierNames.add(queryModifier.getName().trim()));

        for (String searchModeQueryModifier :
                searchModeQueryModifiers) {
            if (queryModifierNames.stream().noneMatch(searchModeQueryModifier.trim()::equalsIgnoreCase)) {
                return false;
            }
        }
        return true;
    }

    public String getCorrespondingSearchMode(Set<QueryModifier> queryModifiers) throws SearchModeNotFound {
        if (queryModifiers.isEmpty()) {
            throw new SearchModeNotFound(); // Exact modifier
        }

        for (CatalogEntry catalogEntry :
                tableICT108_KombinaceModifikatoru) {
            String searchModeCombination = catalogEntry.getLabel();
            String[] searchModeQueryModifiers = searchModeCombination.split("[+]");
            if (equalsQueryModifiersAndSearchMode(searchModeQueryModifiers, queryModifiers)) {
                return catalogEntry.getId();
            }
        }
        throw new SearchModeNotFound();
    }
}
