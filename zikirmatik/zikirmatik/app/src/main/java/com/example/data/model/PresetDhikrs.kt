package com.example.data.model

import com.example.data.db.DhikrEntity

enum class DhikrCategory(val id: String, val displayName: String, val iconName: String) {
    ALL("all", "Tümü", "all"),
    NAMAZ("namaz", "Namaz Tesbihatı", "mosque"),
    GUNLUK("gunluk", "Günlük Zikirler", "sunny"),
    ESMA("esma", "Esmâ-ül Hüsnâ", "sparkle"),
    OZEL("ozel", "Özel Dualar", "book"),
    FAVORITES("favorites", "Favoriler", "star"),
    CUSTOM("kisisel", "Özel Zikirlerim", "add")
}

object PresetDhikrs {
    val list = listOf(
        // Namaz Tesbihatı
        DhikrEntity(
            title = "Sübhânallah",
            arabicText = "سُبْحَانَ اللَّهِ",
            transliteration = "Subhanallah",
            meaning = "Allah her türlü eksiklik ve kusurdan münezzehtir, yücedir.",
            category = "namaz",
            targetCount = 33,
            orderIndex = 1
        ),
        DhikrEntity(
            title = "Elhamdülillâh",
            arabicText = "اَلْحَمْدُ لِلَّهِ",
            transliteration = "Elhamdulillah",
            meaning = "Bütün hamd ve övgüler yalnızca Allah'a aittir.",
            category = "namaz",
            targetCount = 33,
            orderIndex = 2
        ),
        DhikrEntity(
            title = "Allâhu Ekber",
            arabicText = "اللَّهُ أَكْبَرُ",
            transliteration = "Allahu Ekber",
            meaning = "Allah en büyüktür.",
            category = "namaz",
            targetCount = 33,
            orderIndex = 3
        ),
        DhikrEntity(
            title = "Kelime-i Tevhid",
            arabicText = "لَا إِلَهَ إِلَّا اللَّهُ مُحَمَّدٌ رَسُولُ اللَّهِ",
            transliteration = "Lâ ilâhe illallâh Muhammedun Resûlullâh",
            meaning = "Allah'tan başka ilah yoktur, Muhammed (s.a.v) O'nun elçisidir.",
            category = "gunluk",
            targetCount = 100,
            orderIndex = 4
        ),
        DhikrEntity(
            title = "Estağfirullâh el-Azîm",
            arabicText = "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ",
            transliteration = "Estağfirullâhel Azîm",
            meaning = "Yüce olan Allah'tan bağışlanma ve mağfiret dilerim.",
            category = "gunluk",
            targetCount = 100,
            orderIndex = 5
        ),
        DhikrEntity(
            title = "Salavât-ı Şerîfe",
            arabicText = "اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ وَعَلَى آلِ سَيِّدِنَا مُحَمَّدٍ",
            transliteration = "Allahümme salli alâ seyyidinâ Muhammedin ve alâ âli seyyidinâ Muhammed",
            meaning = "Allah'ım! Efendimiz Hz. Muhammed'e ve O'nun âline salât ve selâm eyle.",
            category = "gunluk",
            targetCount = 100,
            orderIndex = 6
        ),
        DhikrEntity(
            title = "Lâ Havle ve Lâ Kuvvete İllâ Billâh",
            arabicText = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ الْعَلِيِّ الْعَظِيمِ",
            transliteration = "Lâ havle ve lâ kuvvete illâ billâhil Aliyyil Azîm",
            meaning = "Güç ve kuvvet ancak pek yüce ve pek büyük olan Allah'ın yardımıyladır.",
            category = "gunluk",
            targetCount = 100,
            orderIndex = 7
        ),
        DhikrEntity(
            title = "Hasbünallâhu ve Ni'mel Vekîl",
            arabicText = "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
            transliteration = "Hasbünallâhu ve ni'mel vekîl",
            meaning = "Allah bize yeter, O ne güzel vekildir.",
            category = "gunluk",
            targetCount = 100,
            orderIndex = 8
        ),
        DhikrEntity(
            title = "Sübhânallâhi ve bi-Hamdihî",
            arabicText = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ سُبْحَانَ اللَّهِ الْعَظِيمِ",
            transliteration = "Sübhânallâhi ve bi-hamdihî sübhânallâhil azîm",
            meaning = "Allah'ı hamd ile tesbih ederim, Yüce Allah'ı noksanlıklardan tenzih ederim.",
            category = "gunluk",
            targetCount = 100,
            orderIndex = 9
        ),

        // Esmâ-ül Hüsnâ
        DhikrEntity(
            title = "Yâ Allâh",
            arabicText = "يَا اَللّٰه",
            transliteration = "Yâ Allâh (Celle Celâluhû)",
            meaning = "Kendisinden başka ilah olmayan, her şeyin mutlak sahibi olan tek yaratıcı.",
            category = "esma",
            targetCount = 66,
            orderIndex = 10
        ),
        DhikrEntity(
            title = "Yâ Rahmân",
            arabicText = "يَا رَحْمٰن",
            transliteration = "Yâ Rahmân (C.C.)",
            meaning = "Dünyada bütün mahlukata sonsuz merhamet eden.",
            category = "esma",
            targetCount = 298,
            orderIndex = 11
        ),
        DhikrEntity(
            title = "Yâ Rahîm",
            arabicText = "يَا رَحِيم",
            transliteration = "Yâ Rahîm (C.C.)",
            meaning = "Ahirette yalnızca mümin kullarına merhamet eden.",
            category = "esma",
            targetCount = 258,
            orderIndex = 12
        ),
        DhikrEntity(
            title = "Yâ Fettâh",
            arabicText = "يَا فَتَّاح",
            transliteration = "Yâ Fettâh (C.C.)",
            meaning = "Her türlü kapalı kapıları, zorlukları ve hayır kapılarını açan.",
            category = "esma",
            targetCount = 489,
            orderIndex = 13
        ),
        DhikrEntity(
            title = "Yâ Rezzâk",
            arabicText = "يَا رَزَّاق",
            transliteration = "Yâ Rezzâk (C.C.)",
            meaning = "Bütün canlıların rızkını bol bol ve vaktinde ihsan eden.",
            category = "esma",
            targetCount = 308,
            orderIndex = 14
        ),
        DhikrEntity(
            title = "Yâ Şâfî",
            arabicText = "يَا شَافِي",
            transliteration = "Yâ Şâfî (C.C.)",
            meaning = "Maddi ve manevi her türlü derde, hastalığa şifa bahşeden.",
            category = "esma",
            targetCount = 391,
            orderIndex = 15
        ),
        DhikrEntity(
            title = "Yâ Vedûd",
            arabicText = "يَا وَدُود",
            transliteration = "Yâ Vedûd (C.C.)",
            meaning = "Kullarını çok seven, sevilmeye en layık olan.",
            category = "esma",
            targetCount = 20,
            orderIndex = 16
        ),
        DhikrEntity(
            title = "Yâ Latîf",
            arabicText = "يَا لَطِيف",
            transliteration = "Yâ Latîf (C.C.)",
            meaning = "Lütuf ve keremi bol olan, her şeye incelikle nüfuz eden.",
            category = "esma",
            targetCount = 129,
            orderIndex = 17
        ),

        // Özel Dualar
        DhikrEntity(
            title = "Yunus (A.S.) Duası",
            arabicText = "لَا إِلَهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ",
            transliteration = "Lâ ilâhe illâ ente sübhâneke innî küntü minez-zâlimîn",
            meaning = "Senden başka hiçbir ilah yoktur. Seni tenzih ederim. Gerçekten ben zalimlerden oldum.",
            category = "ozel",
            targetCount = 100,
            orderIndex = 18
        ),
        DhikrEntity(
            title = "Salât-ı Tefriciye",
            arabicText = "اللَّهُمَّ صَلِّ صَلاَةً كَامِلَةً وَسَلِّمْ سَلاَمًا تَامًّا عَلَى سَيِّدِنَا مُحَمَّدٍ...",
            transliteration = "Allahümme salli salâten kâmileten ve sellim selâmen tâmmen alâ seyyidinâ Muhammedinillezî tenhallü bihil ukad...",
            meaning = "Sıkıntıları gideren, zorlukları çözen, hacetleri karşılayan büyük salavat-ı şerife.",
            category = "ozel",
            targetCount = 4444,
            orderIndex = 19
        ),
        DhikrEntity(
            title = "Âyet-el Kürsî",
            arabicText = "اللَّهُ لَا إِلَهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ...",
            transliteration = "Allâhu lâ ilâhe illâ hüvel hayyul kayyûm...",
            meaning = "Bakara Suresi 255. ayet, tevhid ve ilahi kudretin yüceliği.",
            category = "ozel",
            targetCount = 7,
            orderIndex = 20
        ),
        DhikrEntity(
            title = "Salât-ı Münciye (Tüncînâ)",
            arabicText = "اللَّهُمَّ صَلِّ عَلَى سَيِّدِنَا مُحَمَّدٍ صَلاَةً تُنْجِينَا بِهَا مِنْ جَمِيعِ الْأَهْوَالِ وَالْآفَاتِ...",
            transliteration = "Allâhümme salli alâ seyyidinâ Muhammedin salâten tüncînâ bihâ min cemî'il ehvâli vel âfât...",
            meaning = "Bizi bütün korkulardan ve afetlerden kurtaracak olan bereketli salavat.",
            category = "ozel",
            targetCount = 11,
            orderIndex = 21
        )
    )
}
