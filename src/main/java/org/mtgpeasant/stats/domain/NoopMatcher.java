package org.mtgpeasant.stats.domain;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Builder
@Value
public class NoopMatcher extends Matcher {
    final String card;

    @Override
    public String toString() {
        return "$noop";
    }

    @Override
    public void validate(Validation validation, MatcherContext context) {
    }

    @Override
    public Stream<Match> matches(Stream<Match> stream, MatcherContext context) {
        return stream;
    }

    public List<Match> matches(Match upstream, MatcherContext context) {
        return Collections.singletonList(upstream.select(card));
    }
}
