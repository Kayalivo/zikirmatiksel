package com.example.data.model

data class TasbihatStep(
    val stepIndex: Int,
    val title: String,
    val arabicText: String,
    val turkishPronunciation: String,
    val meaning: String,
    val targetCount: Int,
    val virtue: String
)

object NamazTasbihatData {
    val steps = listOf(
        TasbihatStep(
            stepIndex = 0,
            title = "Sübhanallah",
            arabicText = "سُبْحَانَ اللَّهِ",
            turkishPronunciation = "Sübhânallâh",
            meaning = "Allah her türlü eksiklik ve kusurdan münezzehtir, yücedir.",
            targetCount = 33,
            virtue = "Peygamber Efendimiz (s.a.v.): 'Her namazdan sonra 33 defa Sübhanallah diyenin günahları deniz köpüğü kadar da olsa bağışlanır.' buyurmuştur."
        ),
        TasbihatStep(
            stepIndex = 1,
            title = "Elhamdülillah",
            arabicText = "الْحَمْدُ لِلَّهِ",
            turkishPronunciation = "Elhamdülillâh",
            meaning = "Bütün hamd ve şükürler, alemlerin Rabbi olan Allah'a mahsustur.",
            targetCount = 33,
            virtue = "Mizanda en ağır basan, cennet ehlinin dilinden düşmeyen en faziletli şükür ve zikir ifadesidir."
        ),
        TasbihatStep(
            stepIndex = 2,
            title = "Allahu Ekber",
            arabicText = "اللَّهُ أَكْبَرُ",
            turkishPronunciation = "Allâhu Ekber",
            meaning = "Allah en büyüktür, her şeyden yüce ve azizdir.",
            targetCount = 33,
            virtue = "Kalbi her türlü dünyevi endişe ve kibirden arındıran, Allah'ın azametini ikrar eden ulu zikir."
        ),
        TasbihatStep(
            stepIndex = 3,
            title = "Tehlil & Dua (Tamamlayıcı)",
            arabicText = "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
            turkishPronunciation = "Lâ ilâhe illallâhu vahdehû lâ şerîke leh, lehü'l-mülkü ve lehü'l-hamdü ve hüve alâ külli şey'in kadîr.",
            meaning = "Allah'tan başka hiçbir ilah yoktur; O tektir, ortağı yoktur. Mülk O'nundur, hamd O'nadır ve O her şeye hakkıyla kadirdir.",
            targetCount = 1,
            virtue = "99 tesbihi 100'e tamamlayan, duaların icabetine ve günahların affına vesile olan mühür zikridir."
        )
    )
}
