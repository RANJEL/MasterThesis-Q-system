package cz.komix.qsystem.frontend.controller.query;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jan Lejnar
 */
@Named
@SessionScoped
public class HitCandidatesSessionMapController implements Serializable {
    // == Map<AAAID, HitCandidate>
    private Map<String, StandardQueryHitListType.HitCandidates.HitCandidate> hitCandidatesSessionMap = new ConcurrentHashMap<>();

    /**
     * @param AAAIDType complex element
     * @return encoded AAAID to be used in url in query string
     */
    public static String AAAIDToString(AAAIDType AAAIDType) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(AAAIDType.getNationalIDNumber().getIdNumber());
        stringBuilder.append('_');
        stringBuilder.append(AAAIDType.getNationalIDNumber().getAliasNumber());
        stringBuilder.append('_');
        stringBuilder.append(AAAIDType.getRequestingUser().getValue());
        stringBuilder.append('_');
        stringBuilder.append(AAAIDType.getRecordType().getValue());
        return URLEncoder.encode(stringBuilder.toString(), StandardCharsets.UTF_8);
    }

    public void putHitCandidate(StandardQueryHitListType.HitCandidates.HitCandidate hitCandidate) {
        hitCandidatesSessionMap.put(
                AAAIDToString(hitCandidate.getMetaData().getAAAID()),
                hitCandidate
        );
    }

    public StandardQueryHitListType.HitCandidates.HitCandidate getHitCandidate(String AAAID) {
        return hitCandidatesSessionMap.get(AAAID);
    }
}
