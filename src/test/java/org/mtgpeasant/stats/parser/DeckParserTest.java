package org.mtgpeasant.stats.parser;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mtgpeasant.stats.domain.Cards;
import org.mtgpeasant.stats.domain.Deck;

import java.io.IOException;
import java.io.StringReader;


public class DeckParserTest {
    @Test
    public void should_parse() throws IOException {
        Deck deck = DeckParser.parse(new StringReader("6x swamp\n3 mountains  \n2   Ulamog Crusher\ndark ritual"));
        Assertions.assertThat(deck.getMain().size()).isEqualTo(12);
        Assertions.assertThat(deck.getMain().getCards()).containsExactly(
                "swamp",
                "swamp",
                "swamp",
                "swamp",
                "swamp",
                "swamp",
                "mountains",
                "mountains",
                "mountains",
                "ulamog crusher",
                "ulamog crusher",
                "dark ritual"
        );
    }

    @Test
    public void comment_should_parse() {
        DeckParser.CardLine line = DeckParser.parse("# lightning bolt");
        Assertions.assertThat(line).isNull();
    }

    @Test
    public void basic_name_should_parse() {
        DeckParser.CardLine line = DeckParser.parse("lightning bolt");
        Assertions.assertThat(line).isNotNull();
        Assertions.assertThat(line.getName()).isEqualTo("lightning bolt");
        Assertions.assertThat(line.isMain()).isTrue();
        Assertions.assertThat(line.getCount()).isEqualTo(1);
        Assertions.assertThat(line.getExtension()).isNull();
    }

    @Test
    public void sb1_should_parse() {
        DeckParser.CardLine line = DeckParser.parse("SB:lightning bolt");
        Assertions.assertThat(line).isNotNull();
        Assertions.assertThat(line.getName()).isEqualTo("lightning bolt");
        Assertions.assertThat(line.isMain()).isFalse();
        Assertions.assertThat(line.getCount()).isEqualTo(1);
        Assertions.assertThat(line.getExtension()).isNull();
    }

    @Test
    public void sb2_should_parse() {
        DeckParser.CardLine line = DeckParser.parse("SB lightning bolt");
        Assertions.assertThat(line).isNotNull();
        Assertions.assertThat(line.getName()).isEqualTo("lightning bolt");
        Assertions.assertThat(line.isMain()).isFalse();
        Assertions.assertThat(line.getCount()).isEqualTo(1);
        Assertions.assertThat(line.getExtension()).isNull();
    }

    @Test
    public void number1_should_parse() {
        DeckParser.CardLine line = DeckParser.parse("2 lightning bolt");
        Assertions.assertThat(line).isNotNull();
        Assertions.assertThat(line.getName()).isEqualTo("lightning bolt");
        Assertions.assertThat(line.isMain()).isTrue();
        Assertions.assertThat(line.getCount()).isEqualTo(2);
        Assertions.assertThat(line.getExtension()).isNull();
    }

    @Test
    public void number2_should_parse() {
        DeckParser.CardLine line = DeckParser.parse("2x lightning bolt");
        Assertions.assertThat(line).isNotNull();
        Assertions.assertThat(line.getName()).isEqualTo("lightning bolt");
        Assertions.assertThat(line.isMain()).isTrue();
        Assertions.assertThat(line.getCount()).isEqualTo(2);
        Assertions.assertThat(line.getExtension()).isNull();
    }

    @Test
    public void ext_should_parse() {
        DeckParser.CardLine line = DeckParser.parse("2x [FBB] lightning bolt");
        Assertions.assertThat(line).isNotNull();
        Assertions.assertThat(line.getName()).isEqualTo("lightning bolt");
        Assertions.assertThat(line.isMain()).isTrue();
        Assertions.assertThat(line.getCount()).isEqualTo(2);
        Assertions.assertThat(line.getExtension()).isEqualTo("FBB");
    }

    @Test
    public void full1_should_parse() {
        DeckParser.CardLine line = DeckParser.parse("SB: 2 [FBB] lightning bolt");
        Assertions.assertThat(line).isNotNull();
        Assertions.assertThat(line.getName()).isEqualTo("lightning bolt");
        Assertions.assertThat(line.isMain()).isFalse();
        Assertions.assertThat(line.getCount()).isEqualTo(2);
        Assertions.assertThat(line.getExtension()).isEqualTo("FBB");
    }

}
