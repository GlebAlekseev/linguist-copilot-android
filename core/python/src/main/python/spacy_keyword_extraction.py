import spacy
import spacy_ke
import json

nlp = spacy.load("en_core_web_sm")


def process(text, number_words):
    # spacy v3.0.x factory.
    # if you're using spacy v2.x.x swich to `nlp.add_pipe(spacy_ke.Yake(nlp))`
    # nlp.add_pipe("yake")
    nlp.add_pipe(spacy_ke.Yake(nlp))

    doc = nlp(text)

    keyword_list = []
    for keyword, score in doc._.extract_keywords(n=number_words):
        keyword_list.append(
            {
                "keyword": str(keyword),
                "score": score,
            })

    return json.dumps(keyword_list)

if __name__ == "__main__":
    text = """
    Iron is a chemical element; it has symbol Fe (from Latin ferrum 'iron') and atomic number 26. It is a metal that belongs to the first transition series and group 8 of the periodic table. It is, by mass, the most common element on Earth, forming much of Earth's outer and inner core. It is the fourth most common element in the Earth's crust, being mainly deposited by meteorites in its metallic state.

    Extracting usable metal from iron ores requires kilns or furnaces capable of reaching 1,500 °C (2,730 °F), about 500 °C (932 °F) higher than that required to smelt copper. Humans started to master that process in Eurasia during the 2nd millennium BC and the use of iron tools and weapons began to displace copper alloys – in some regions, only around 1200 BC. That event is considered the transition from the Bronze Age to the Iron Age. In the modern world, iron alloys, such as steel, stainless steel, cast iron and special steels, are by far the most common industrial metals, due to their mechanical properties and low cost. The iron and steel industry is thus very important economically, and iron is the cheapest metal, with a price of a few dollars per kilogram or pound.

    Pristine and smooth pure iron surfaces are a mirror-like silvery-gray. Iron reacts readily with oxygen and water to produce brown-to-black hydrated iron oxides, commonly known as rust. Unlike the oxides of some other metals that form passivating layers, rust occupies more volume than the metal and thus flakes off, exposing more fresh surfaces for corrosion. Chemically, the most common oxidation states of iron are iron(II) and iron(III). Iron shares many properties of other transition metals, including the other group 8 elements, ruthenium and osmium. Iron forms compounds in a wide range of oxidation states, −4 to +7. Iron also forms many coordination compounds; some of them, such as ferrocene, ferrioxalate, and Prussian blue have substantial industrial, medical, or research applications.

    The body of an adult human contains about 4 grams (0.005% body weight) of iron, mostly in hemoglobin and myoglobin. These two proteins play essential roles in oxygen transport by blood and oxygen storage in muscles. To maintain the necessary levels, human iron metabolism requires a minimum of iron in the diet. Iron is also the metal at the active site of many important redox enzymes dealing with cellular respiration and oxidation and reduction in plants and animals.[9]
    """

    result = process(text, 10)
    print(result)
