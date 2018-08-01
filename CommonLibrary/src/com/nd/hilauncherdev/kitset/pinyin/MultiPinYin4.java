package com.nd.hilauncherdev.kitset.pinyin;

/**
 * 多音节，从0x7F37到0x8F9E
 */
public class MultiPinYin4 {
	private static String [] result;
	
	private MultiPinYin4() {
	};

	public static String[] getMultiPinYin4() {
		if (result == null)
			result = new String[] { "xie", "gang", "fou", "que", "fou", "que", "bo", "ping", "hou", "none", "gang", "ying", "ying", "qing", "xia", "guan", "zun", "tan", "none", "qing", "weng", "ying",
				"lei", "tan", "lu", "guan", "wang", "gang", "wang", "wang", "han", "none", "luo", "fu,fou", "mi", "fa", "gu", "zhu", "ju", "mao", "gu", "min", "gang", "ba", "gua", "ti", "juan", "fu",
				"lin,sen", "yan", "zhao", "zui", "gua", "zhuo", "yu", "zhi", "an", "fa", "lan", "shu", "si", "pi", "ma", "liu", "ba,pi", "fa", "li", "chao", "wei", "bi", "ji", "zeng", "tong", "liu",
				"ji", "juan", "mi", "zhao", "luo", "pi,biao", "ji", "ji", "luan", "yang", "mie", "qiang", "ta", "mei", "yang", "you", "you", "fen", "ba", "gao", "yang", "gu", "qiang", "zang", "gao",
				"ling", "yi", "zhu", "di", "xiu", "qiang", "yi", "xian", "rong", "qun", "qun", "qian,qiang", "huan", "suo", "xian", "yi", "yang", "qiang", "xian", "yu", "geng", "jie", "tang", "yuan",
				"xi", "fan", "shan", "fen", "shan", "lian", "lei", "geng", "nou", "qiang", "chan", "yu", "gong", "yi", "chong", "weng", "fen", "hong", "chi", "chi", "cui", "fu,pei", "xia", "pen",
				"yi", "la", "yi", "pi,po", "ling", "liu", "zhi", "qu", "xi", "xie", "xiang", "xi", "xi", "qi", "qiao", "hui", "hui", "shu", "se", "hong", "jiang", "zhai,di", "cui", "fei", "tao",
				"sha", "chi", "zhu", "jian", "xuan", "shi", "pian", "zong", "wan", "hui", "hou", "he", "he", "han", "ao", "piao", "yi", "lian", "qu", "none", "lin", "pen", "qiao", "ao", "fan", "yi",
				"hui", "xuan", "dao", "yao,yue", "lao", "none", "kao", "mao", "zhe", "qi", "gou", "gou", "gou", "die", "die", "er", "shua", "ruan", "er", "nai", "zhuan,duan", "lei", "ting", "zi",
				"geng", "chao", "hao", "yun", "pa,ba", "pi", "chi", "si", "qu", "jia", "ju", "huo", "chu", "lao", "lun", "ji", "tang", "ou", "lou", "nou", "jiang", "pang", "ze", "lou", "ji", "lao",
				"huo", "you", "mo", "huai", "er", "zhe", "ding", "ye", "da", "song", "qin", "yun", "chi", "dan", "dan", "hong", "geng", "zhi", "none", "nie", "dan", "zhen", "che", "ling", "zheng",
				"you", "wa", "liao", "long", "zhi", "ning", "tiao", "er", "ya", "die", "guo,gua", "none", "lian", "hao", "sheng", "lie", "pin", "jing", "ju", "bi", "di", "guo", "wen", "xu", "ping",
				"cong", "none", "none", "ting", "yu", "cong", "kui", "lian", "kui", "cong", "lian", "weng", "kui", "lian", "lian", "cong", "ao", "sheng", "song", "ting", "kui", "nie", "zhi", "dan",
				"ning", "none", "ji", "ting", "ting", "long", "yu", "yu", "zhao", "si", "su", "yi", "su", "si", "zhao", "zhao", "rou", "yi", "lei", "ji", "qiu", "ken", "cao", "ge", "di", "huan",
				"huang", "yi", "ren", "xiao", "ru", "zhou", "yuan", "du", "gang", "rong", "gan", "cha", "wo", "chang", "gu", "zhi", "qin", "fu", "fei", "ban", "pei", "pang", "jian", "fang", "zhun",
				"you", "na", "ang", "ken", "ran", "gong", "yu,yo", "wen", "yao", "jin", "pi", "qian", "xi", "xi", "fei", "ken", "jing", "tai", "shen", "zhong", "zhang", "xie", "shen", "wei", "zhou",
				"die", "dan", "fei", "ba", "bo", "qu", "tian", "bei", "gua", "tai", "zi", "ku", "zhi", "ni", "ping", "zi", "fu", "pang", "zhen", "xian", "zuo", "pei", "jia", "sheng", "zhi", "bao",
				"mu", "qu", "hu", "ke", "yi", "yin", "xu", "yang", "long", "dong", "ka", "lu", "jing", "nu", "yan", "pang", "kua", "yi", "guang", "hai,gai", "ge,ga", "dong", "zhi", "jiao", "xiong",
				"xiong", "er", "an", "xing", "pian", "neng", "zi", "none", "cheng", "tiao", "zhi", "cui", "mei", "xie", "cui", "xie", "mo,mai", "mai,mo", "ji", "xie", "none", "kuai", "sa", "zang",
				"qi", "nao", "mi", "nong", "luan", "wan", "bo", "wen", "wan", "qiu", "jiao,jue", "jing", "you", "heng", "cuo", "lie", "shan", "ting", "mei", "chun", "shen", "xie", "none", "juan",
				"cu", "xiu", "xin", "tuo", "pao", "cheng", "nei", "fu,pu", "dou", "tuo", "niao", "nao", "pi", "gu", "luo", "li", "lian", "zhang", "cui", "jie", "liang", "shui", "pi", "biao", "lun",
				"pian", "guo", "juan", "chui,zhui", "dan", "tian", "nei", "jing", "jie", "la,xi", "ye,yi", "a,yan", "ren", "shen", "chuo,duo", "fu", "fu", "ju", "fei", "qiang", "wan", "dong", "pi",
				"guo", "zong", "ding", "wu", "mei", "ruan", "zhuan,dun", "zhi", "cou", "gua,luo", "ou", "di", "an", "xing", "nao", "shu", "shuan", "nan", "yun", "zhong", "rou", "e", "sai", "tu",
				"yao", "jian", "wei", "jiao", "yu", "jia", "duan", "bi", "chang", "fu", "xian", "ni", "mian", "wa", "teng", "tui", "bang,pang", "qian", "lu:", "wa", "shou", "tang", "su", "zhui",
				"ge", "yi", "bo", "liao", "ji", "pi", "xie", "gao", "lu:", "bin", "none", "chang", "lu", "guo", "pang", "chuai", "biao", "jiang", "fu", "tang", "mo", "xi", "zhuan", "lu:", "jiao",
				"ying", "lu:", "zhi", "xue", "chun", "lin", "tong", "peng", "ni", "chuai", "liao", "cui", "gui", "xiao", "teng", "fan", "zhi", "jiao", "shan", "hu", "cui", "run", "xin", "sui", "fen",
				"ying", "shan", "gua", "dan", "kuai", "nong", "tun", "lian", "bei,bi", "yong", "jue", "chu", "yi", "juan", "la,xi", "lian", "sao", "tun", "gu", "qi", "cui", "bin", "xun", "nao,ru",
				"huo", "zang", "xian", "biao", "xing", "kuan", "la,xi", "yan", "lu", "hu", "za", "luo", "qu", "zang", "luan", "ni", "za", "chen", "qian", "wo", "guang,wang", "zang", "lin", "guang",
				"zi", "jiao", "nie", "chou,xiu", "ji", "gao", "chou,xiu", "mian", "nie", "zhi", "zhi", "ge", "jian", "die", "zhi", "xiu", "tai", "zhen", "jiu", "xian", "yu", "cha", "yao", "yu",
				"chong", "xi", "xi", "jiu", "yu", "yu", "xing", "ju", "jiu", "xin", "she", "she", "she", "jiu", "shi", "tan", "shu", "shi", "tian", "dan", "pu", "pu", "guan", "hua", "tian", "chuan",
				"shun", "xia", "wu", "zhou", "dao", "chuan", "shan", "yi", "none", "pa", "tai", "fan", "ban", "chuan", "hang", "fang", "ban,bo,pan", "bi", "lu", "zhong", "jian", "cang", "ling",
				"zhu", "ze", "duo,tuo", "bo", "xian", "ge", "chuan", "jia,xia", "lu", "hong", "pang", "xi", "none", "fu", "zao", "feng", "li", "shao", "yu", "lang", "ting", "none", "wei", "bo",
				"meng", "nian", "ju", "huang", "shou", "zong", "bian", "mao", "die", "none", "bang", "cha", "yi", "sou,sao", "cang", "cao", "lou", "dai", "none", "yao", "chong,tong", "none", "dang",
				"qiang", "lu", "yi", "jie", "jian", "huo", "meng", "qi", "lu", "lu", "chan", "shuang", "gen", "liang", "jian", "jian", "se,shai", "yan", "fu", "ping", "yan", "yan", "cao", "cao",
				"yi", "le", "ting", "jiao", "ai,yi", "nai", "tiao", "jiao", "jie", "peng", "wan", "yi", "chai", "mian", "mi", "gan", "qian", "yu", "yu", "shao", "xiong", "du", "xia", "qi",
				"mang,wang", "zi", "hui", "sui", "zhi", "xiang", "bi,pi", "fu", "tun", "wei", "wu", "zhi", "qi", "shan", "wen", "qian", "ren", "fou", "kou", "jie,gai", "lu", "zhu", "ji", "qin", "qi",
				"yan,yuan", "fen", "ba", "rui", "xin", "ji", "hua", "hua", "fang", "wu", "jue", "gou", "zhi", "yun", "qin", "ao", "chu", "mao", "ya,di", "fei,fu", "reng", "hang", "cong", "yin",
				"you", "bian", "yi", "none", "wei", "li", "pi", "e", "xian", "chang", "cang", "zhu,ning", "su", "yi,ti", "yuan", "ran", "ling", "tai", "tiao,shao", "di", "miao", "qing", "li", "rao",
				"ke", "mu", "pei", "bao", "gou", "min", "yi", "yi", "ju,qu", "pie", "ruo,re", "ku", "zhu,ning", "ni", "bo", "bing", "shan", "qiu", "yao", "xian", "ben", "hong", "ying", "zha", "dong",
				"ju", "die", "nie", "gan", "hu", "ping", "mei", "fu", "sheng", "gu", "bi", "wei", "fu", "zhuo", "mao", "fan", "qie,jia", "mao", "mao", "ba", "zi,ci", "mo", "zi", "di", "chi", "gou",
				"jing", "long", "none", "niao", "none", "xue", "ying", "qiong", "ge", "ming", "li", "rong", "yin", "gen", "qian,xi", "chai", "chen", "yu", "xiu", "zi", "lie", "wu", "duo", "kui",
				"ce", "jian", "ci", "gou", "guang", "mang", "cha,zha", "jiao", "jiao", "fu", "yu", "zhu", "zi", "jiang", "hui", "yin", "cha", "fa", "rong", "ru", "chong", "mang", "tong", "zhong",
				"none", "zhu", "xun", "huan", "kua", "quan", "gai", "da", "jing", "xing", "chuan", "cao", "jing", "er", "an", "shou", "chi", "ren", "jian", "ti,yi", "huang", "ping", "li", "jin",
				"lao,pei", "rong", "zhuang", "da", "jia", "rao", "bi", "ce", "qiao", "hui", "ji,qi", "dang", "none", "rong", "hun,xun", "ying,xing", "luo", "ying", "qian,xun", "jin", "sun", "yin",
				"mai", "hong", "zhou", "yao", "du", "wei", "chu", "dou", "fu", "ren", "yin", "he", "bi", "bu", "yun", "di", "tu", "sui", "sui", "cheng", "chen", "wu", "bie", "xi", "geng", "li", "pu",
				"zhu", "mo", "li", "zhuang", "ji", "duo", "qiu", "sha,suo", "suo", "chen", "feng", "ju", "mei", "meng", "xing", "jing", "che", "xin,shen", "jun", "yan", "ting", "you", "cuo",
				"guan,wan", "han", "you", "cuo", "jia", "wang", "you", "niu,chou", "shao", "xian", "lang,liang", "fu,piao", "e", "mo", "wen", "jie", "nan", "mu", "kan", "lai", "lian", "shi",
				"wo,zhua", "tu", "xian", "huo", "you", "ying", "ying", "none", "chun", "mang", "mang", "ci", "yu,wan", "jing", "di", "qu", "dong", "jian", "zou", "gu", "la", "lu,lu:", "ju", "wei",
				"jun", "nie", "kun", "he", "pu", "zai", "gao", "guo", "fu", "lun", "chang", "chou", "song", "chui", "zhan", "men", "cai", "ba", "li", "tu", "bo", "han", "bao", "qin", "juan", "xi",
				"qin", "di", "jie", "pu", "dang", "jin", "zhao", "tai", "geng", "hua", "gu", "ling", "fei", "jin", "an", "wang", "beng", "zhou", "yan", "zu", "jian", "lin", "tan", "shu", "tian",
				"dao", "hu", "ji,qi", "he", "cui", "tao", "chun", "bei,bi", "chang", "huan", "fei", "lai", "qi", "meng", "ping", "wei", "dan", "sha", "huan", "yan", "yi", "tiao", "qi,ji", "wan",
				"ce", "nai", "none", "tuo", "jiu", "tie", "luo", "none", "none", "meng", "none", "none", "ding", "ying", "ying", "ying", "xiao", "sa", "qiu", "ke", "xiang", "wan,mo", "yu", "yu",
				"fu", "lian", "xuan", "xuan", "nan", "ze", "wo", "chun", "xiao", "yu", "pian,bian", "mao", "an", "e", "luo,la,lao", "ying", "huo", "gua", "jiang", "wan", "zuo", "zuo", "ju", "bao",
				"rou", "xi", "xie,ye,she", "an", "qu", "jian", "fu", "lu:", "lu:", "pen", "feng", "hong", "hong", "hou", "yan", "tu", "zhu,zhe,zhao,zi,zhuo", "zi", "xiang", "shen,ren", "ge", "qia",
				"jing", "mi", "huang", "shen", "pu", "ge", "dong", "zhou", "qian", "wei", "bo", "wei", "pa", "ji", "hu", "zang", "jia", "duan", "yao", "jun", "cong", "quan", "wei", "xian", "kui",
				"ting", "hun,xun", "xi", "shi", "qi", "lan", "zong", "yao", "yuan", "mei", "yun", "shu", "di", "zhuan", "guan", "none", "qiong", "chan", "kai", "kui", "none", "jiang", "lou", "wei",
				"pai", "none", "sou", "yin", "shi", "chun", "shi", "yun", "zhen", "lang", "nu", "meng", "he", "que", "suan", "yuan", "li", "ju", "xi", "bang,pang", "chu", "xu", "tu", "liu", "huo",
				"zhen", "qian", "zu", "po", "cuo", "yuan", "chu", "yu", "kuai", "pan", "pu", "pu", "na", "shuo", "xi", "fen", "yun", "zheng", "jian", "ji", "ruo", "cang", "en", "mi", "hao", "sun",
				"zhen", "ming", "none", "xu", "liu", "xi", "gu", "lang", "rong", "weng", "gai,ge,he", "cuo", "shi", "tang", "luo", "ru", "suo", "xian", "bei", "yao", "gui", "bi", "zong", "gun",
				"none", "xiu", "ce", "none", "lan", "none", "ji", "li", "can", "lang", "yu", "none", "ying", "mo", "diao", "xiu", "wu", "tong", "zhu", "peng", "an", "lian", "cong", "xi", "ping",
				"qiu,ou", "jin", "chun", "jie", "wei", "tui", "cao", "yu", "yi", "ji", "liao,lu", "bi", "lu", "xu,su", "bu", "zhang", "luo", "qiang", "man", "yan", "leng", "ji", "biao,piao", "gun",
				"han", "di", "su", "lu", "she", "shang", "di", "mie", "xun", "man,wan", "bo,bu", "di", "cuo", "zhe", "sen", "xuan", "yu,wei", "hu", "ao", "mi", "lou", "cu", "zhong", "cai", "po",
				"jiang", "mi", "cong", "niao", "hui", "jun", "yin", "jian", "nian", "shu", "yin", "kui", "chen", "hu", "sha", "kou", "qian", "ma", "cang,zang", "ze", "qiang", "dou", "lian", "lin",
				"kou", "ai", "bi", "li", "wei", "ji", "qian,xun", "sheng", "fan,bo", "meng", "ou", "chan", "dian", "xun,jun", "jiao,qiao", "rui", "rui", "lei", "yu", "qiao", "chu", "hua", "jian",
				"mai", "yun", "bao", "you", "qu", "lu", "rao", "hui", "e", "ti", "fei", "jue", "zui", "fa", "ru", "fen", "kui", "shun", "rui", "ya", "xu", "fu", "jue", "dang", "wu", "tong", "si",
				"xiao", "xi", "yong", "wen", "shao", "qi", "jian", "yun", "sun", "ling", "yu", "xia", "weng", "ji", "hong", "si", "deng", "lei", "xuan", "yun", "yu", "xi", "hao", "bo,bao", "hao",
				"ai", "wei", "hui", "wei", "ji", "ci", "xiang", "luan", "mie", "yi", "leng", "jiang", "can", "shen,can,cen", "qiang", "lian", "ke", "yuan", "da", "ti", "tang", "xue", "bi,bo", "zhan",
				"sun", "lian,xian", "fan", "ding", "xiao", "gu", "xie", "shu", "jian", "kao", "hong", "sa", "xin", "xun", "yao", "bai", "sou", "shu", "xun", "dui", "pin", "wei", "neng", "chou",
				"mai", "ru", "piao", "tai", "qi,ji", "zao", "chen", "zhen", "er", "ni", "ying", "gao", "cong", "xiao", "qi", "fa", "jian", "xu", "kui", "jie", "bian", "di", "mi", "lan", "jin",
				"zang,cang", "miao", "qiong", "qie", "xian,li", "none", "ou", "xian", "su", "lu:", "yi", "xu", "xie", "li", "yi", "la", "lei", "jiao", "di", "zhi", "pi", "teng", "yao,yue", "mo",
				"huan", "biao", "fan", "sou", "tan", "tui", "qiong", "qiao", "wei", "liu", "hui", "shu", "gao", "yun", "none", "li", "zhu,shu", "zhu", "ai", "lin", "zao", "xuan", "chen", "lai",
				"huo", "tuo", "wu,e", "rui", "rui", "qi", "heng", "lu", "su", "tui", "mang", "yun", "pin,ping", "yu", "xun", "ji", "jiong", "xuan", "mo", "none", "su", "jiong", "none", "nie",
				"bo,nie", "rang", "yi", "xian,li", "yu", "ju", "lian", "lian", "yin", "qiang", "ying", "long", "tou", "wei", "yue", "ling", "qu", "yao", "fan", "mi", "lan", "kui", "lan", "ji",
				"dang", "none", "lei", "lei", "tong", "feng", "zhi", "wei", "kui", "zhan", "huai", "li", "ji", "mi", "lei", "huai", "luo", "ji", "nao", "lu", "jian", "none", "none", "lei", "quan",
				"xiao", "yi", "luan", "men", "bie", "hu", "hu", "lu", "nu:e", "lu:", "zhi", "xiao", "qian", "chu", "hu", "xu", "cuo", "fu", "xu", "xu", "lu", "hu", "yu", "hao", "jiao", "ju", "guo",
				"bao", "yan", "zhan", "zhan", "kui", "ban", "xi", "shu", "chong,hui", "qiu", "diao", "ji", "qiu", "ding", "shi", "none", "di", "zhe", "she,yi", "yu", "gan", "zi", "hong,jiang", "hui",
				"meng", "ge", "sui", "xia,ha", "chai", "shi", "yi", "ma", "xiang", "fang", "e", "pa", "chi", "qian", "wen", "wen", "rui", "bang,beng", "pi", "yue", "yue", "jun", "qi", "ran", "yin",
				"qi,chi", "can,tian", "yuan", "jue", "hui", "qian", "qi", "zhong", "ya", "hao", "mu", "wang", "fen", "fen", "hang", "gong", "zao", "fu", "ran", "jie", "fu", "chi", "dou", "bao",
				"xian", "ni", "te", "qiu", "you", "zha", "ping", "chi", "you", "he,ke", "han", "ju", "li", "fu", "ran", "zha", "gou", "pi", "bo", "xian", "zhu", "diao", "bie", "bing", "gu", "ran",
				"qu,ju", "she,yi", "tie", "ling", "gu", "dan", "gu", "ying", "li", "cheng", "qu", "mou", "ge", "ci", "hui", "hui", "mang", "fu", "yang", "wa", "lie", "zhu", "yi", "xian", "kuo",
				"jiao", "li", "yi", "ping", "ji", "ha,ge", "she", "yi", "wang", "mo", "qiong", "qie", "gui", "gong", "zhi", "man", "none", "zhe", "jia", "nao", "si", "qi", "xing", "lie", "qiu",
				"shao,xiao", "yong", "jia", "tui,shui", "che", "bai", "e,yi", "han", "shu", "xuan", "feng", "shen", "zhen", "fu", "xian", "zhe", "wu", "fu", "li", "lang", "bi", "chu", "yuan", "you",
				"jie", "dan", "yan", "ting", "dian", "tui", "hui", "wo", "zhi", "song", "fei", "ju", "mi", "qi", "qi", "yu", "jun", "la,zha", "meng", "qiang", "si", "xi", "lun", "li", "die", "tiao",
				"tao", "kun", "gan", "han", "yu", "bang,beng", "fei", "pi", "wei", "dun", "yi", "yuan", "su", "quan", "qian", "rui", "ni", "qing", "wei", "liang", "guo", "wan", "dong", "e", "ban",
				"zhuo", "wang", "can", "yang", "ying", "guo", "chan", "none", "la,zha", "ke", "ji", "xie,he", "ting", "mai", "xu", "mian", "yu", "jie", "shi", "xuan", "huang", "yan", "bian", "rou",
				"wei", "fu", "yuan", "mei", "wei", "fu", "ruan", "xie", "you", "you,qiu", "mao", "xia,ha", "ying", "shi", "chong", "tang", "zhu", "zong", "ti", "fu", "yuan", "kui", "meng", "la",
				"du", "hu", "qiu", "die", "li,xi", "gua,wo", "yun", "ju", "nan", "lou", "chun", "rong", "ying", "jiang", "tun", "lang", "pang", "si,shi", "xi", "xi", "xi", "yuan", "weng", "lian",
				"sou", "ban", "rong", "rong", "ji", "wu", "xiu", "han", "qin", "yi", "bi", "hua", "tang", "yi", "du", "nai", "he", "hu", "xi", "ma", "ming", "yi", "wen", "ying", "teng", "yu", "cang",
				"none", "none", "man", "none", "shang", "shi,zhe", "cao", "chi", "di", "ao", "lu", "wei", "zhi", "tang", "chen", "piao", "qu", "pi", "yu", "jian", "luo", "lou", "qin", "zhong", "yin",
				"jiang", "shuai,shuo", "wen", "jiao", "wan", "zhe,zhi", "zhe", "ma", "ma", "guo", "liao", "mao", "xi", "cong", "li", "man", "xiao", "none", "zhang", "mang", "xiang", "mo", "zi", "si",
				"qiu", "te", "zhi", "peng", "peng", "jiao", "qu", "bie", "liao", "pan", "gui", "xi", "ji", "zhuan", "huang", "fei", "lao", "jue", "jue", "hui", "yin", "chan", "jiao", "shan",
				"rao,nao", "xiao", "wu", "chong", "xun", "si", "none", "cheng", "dang", "li", "xie", "shan", "yi", "jing", "da", "chan", "qi", "zi", "xiang", "she", "luo", "qin", "ying", "chai",
				"li", "ze", "xuan", "lian", "zhu", "ze", "xie", "mang", "xie", "qi", "rong", "jian", "meng", "hao", "ru,ruan", "huo", "zhuo", "jie", "bin", "he", "mie", "fan", "lei", "jie", "la,zha",
				"mi", "li", "chun", "li", "qiu", "nie", "lu", "du", "xiao", "zhu", "long", "li", "long", "feng", "ye", "pi", "rang", "gu", "juan", "ying", "none", "xi", "can", "qu", "quan", "du",
				"can", "man", "qu", "jie", "zhu", "zha", "xue,xie", "huang", "nu:", "pei", "nu:", "xin", "zhong", "mo", "er", "mie", "mie", "shi", "xing,hang,heng", "yan", "kan", "yuan", "none",
				"ling", "xuan", "shu,zhu", "xian", "tong", "long", "jie", "xian", "ya", "hu", "wei", "dao", "chong", "wei", "dao", "zhun", "heng", "qu", "yi", "yi", "bu", "gan", "yu", "biao", "cha",
				"yi", "shan", "chen", "fu", "gun", "fen", "shuai,cui", "jie", "na", "zhong", "dan", "ri", "zhong", "zhong", "xie", "qi,zhi", "xie", "ran", "zhi", "ren", "qin", "jin", "jun", "yuan",
				"mei", "chai", "ao", "niao", "hui", "ran", "jia", "tuo", "ling", "dai", "bao", "pao", "yao", "zuo", "bi", "shao", "tan", "ju", "he", "xue", "xiu", "zhen", "yi", "pa", "bo,fu", "di",
				"wa", "fu", "gun", "zhi", "zhi", "ran", "pan", "yi", "mao", "none", "na", "kou", "xuan", "chan", "qu", "bei,pi", "yu", "xi", "none", "bo", "none", "fu", "yi", "chi", "ku", "ren",
				"jiang", "jia,qia", "cun", "mo", "jie", "er", "ge", "ru", "zhu", "gui", "yin", "cai", "lie", "none", "none", "zhuang", "dang", "none", "kun", "ken", "niao", "shu", "jia", "kun",
				"cheng", "li", "juan", "shen", "pou", "ge", "yi", "yu", "chen", "liu", "qiu", "qun", "ji", "yi", "bu", "zhuang", "shui", "sha", "qun", "li", "lian", "lian", "ku", "jian", "fou",
				"tan", "bi,pi,bei", "gun", "tao", "yuan", "ling", "chi", "chang", "chou", "duo", "biao", "liang", "shang,chang", "pei", "pei", "fei", "yuan", "luo", "guo", "yan", "du", "ti,xi",
				"zhi", "ju", "qi", "ji", "zhi", "gua", "ken", "none", "ti", "shi", "fu", "chong", "xie", "bian", "die", "kun,hui", "duan", "xiu", "xiu", "he", "yuan", "bao", "bao", "fu", "yu",
				"tuan", "yan", "hui", "bei", "chu,zhu", "lu:", "none", "none", "yun", "ta", "gou", "da", "huai", "rong", "yuan", "ru", "nai", "jiong", "suo", "ban", "tun,tui", "chi", "sang", "niao",
				"ying", "jie", "qian", "huai", "ku", "lian", "lan", "li", "zhe,xi", "shi", "lu:", "yi", "die", "xie", "xian", "wei", "biao", "cao", "ji", "qiang", "sen", "bao", "xiang", "none", "pu",
				"jian", "zhuan", "jian", "zui", "ji", "dan", "za", "fan", "bo", "xiang", "xin", "bie", "rao", "man", "lan", "ao", "duo", "hui", "cao", "sui", "nong", "chan", "lian", "bi", "jin",
				"dang", "shu", "tan", "bi", "lan", "pu", "ru", "zhi", "none", "shu", "wa", "shi", "bai", "xie", "bo", "chen", "lai", "long", "xi", "xian", "lan", "zhe", "dai", "none", "zan", "shi",
				"jian", "pan", "yi", "none", "ya", "xi", "xi", "yao", "feng", "tan,qin", "none", "none", "fu", "ba", "he", "ji", "ji", "jian,xian", "guan", "bian", "yan", "gui", "jue,jiao", "pian",
				"mao", "mi", "mi", "mie", "shi", "si", "zhan,chan", "luo", "jue,jiao", "mo", "tiao", "lian", "yao", "zhi", "jun", "xi", "shan", "wei", "xi", "tian", "yu", "lan", "e", "du",
				"qin,qing", "pang", "ji", "ming", "ping", "gou", "qu", "zhan", "jin", "guan", "deng", "jian", "luo", "qu", "jian", "wei", "jue,jiao", "qu", "luo", "lan", "shen", "di", "guan",
				"jian,xian", "guan", "yan", "gui", "mi", "shi", "chan", "lan", "jue,jiao", "ji", "xi", "di", "tian", "yu", "gou", "jin", "qu", "jiao,jue", "jiu", "jin", "cu", "jue", "zhi", "chao",
				"ji", "gu", "dan", "zui,zi", "di", "shang", "hua", "quan", "ge", "zhi", "jie,xie", "gui", "gong", "chu", "jie,xie", "huan", "qiu", "xing", "su", "ni", "ji", "lu", "zhi", "zhu", "bi",
				"xing", "hu", "shang", "gong", "zhi", "xue", "chu", "xi", "yi", "li", "jue", "xi", "yan", "xi", "yan", "yan", "ding", "fu", "qiu", "qiu", "jiao", "hong", "ji", "fan", "xun", "diao",
				"hong", "cha", "tao", "xu", "jie", "yi", "ren", "xun", "yin", "shan", "qi", "tuo", "ji", "xun", "yin", "e", "fen", "ya", "yao", "song", "shen", "yin", "xin", "jue", "xiao", "ne,na",
				"chen", "you", "zhi", "xiong", "fang", "xin", "chao", "she", "xian", "sa", "zhun", "xu,hu", "yi", "yi", "su", "chi", "he", "shen", "he", "xu", "zhen", "zhu", "zheng", "gou", "zi",
				"zi", "zhan", "gu", "fu", "jian", "die", "ling", "di", "yang", "li", "nao", "pan", "zhou", "gan", "shi", "ju", "ao", "zha", "tuo", "yi", "qu", "zhao", "ping", "bi", "xiong", "chu,qu",
				"ba", "da", "zu", "tao", "zhu", "ci", "zhe", "yong", "xu", "xun", "yi", "huang", "he", "shi", "cha", "jiao", "shi", "hen", "cha", "gou", "gui", "quan", "hui", "jie", "hua", "gai",
				"xiang", "hui", "shen", "chou", "tong", "mi", "zhan", "ming", "e", "hui", "yan", "xiong", "gua", "er", "beng", "tiao,diao", "chi", "lei", "zhu", "kuang", "kua", "wu", "yu", "teng",
				"ji", "zhi", "ren", "su", "lang", "e", "kuang", "e^", "shi", "ting", "dan", "bei", "chan", "you", "heng", "qiao", "qin", "shua", "an", "yu", "xiao", "cheng", "jie", "xian", "wu",
				"wu", "gao", "song", "pu", "hui", "jing", "shuo,shui,yue", "zhen", "shuo,shui,yue", "du,dou", "none", "chang", "shui,shei", "jie", "ke", "qu", "cong", "xiao", "sui", "wang", "xuan",
				"fei", "chi", "ta", "yi", "na", "yin", "diao,tiao", "pi", "chuo", "chan", "chen", "zhun", "ji", "qi", "tan", "chui", "wei", "ju", "qing", "jian", "zheng", "ze", "zou", "qian", "zhuo",
				"liang", "jian", "zhu", "hao", "lun", "shen", "biao", "huai", "pian", "yu", "die", "xu", "pian", "shi", "xuan", "shi", "hun", "hua", "e", "zhong", "di", "xie", "fu", "pu", "ting",
				"jian", "qi", "yu", "zi", "chuan", "xi", "hui", "yin", "an", "xian", "nan", "chen", "feng", "zhu", "yang", "yan", "heng", "xuan", "ge", "nuo", "qi", "mou", "ye", "wei", "none",
				"teng", "zou,zhou", "shan", "jian", "bo", "none", "huang", "huo", "ge", "ying", "mi,mei", "xiao,sou", "mi", "xi", "qiang", "chen", "nu:e,xue", "si", "su", "bang", "chi", "qian",
				"shi,yi", "jiang", "yuan", "xie", "xue", "tao", "yao", "yao", "hu", "yu", "biao", "cong", "qing", "li", "mo", "mo", "shang", "zhe", "miu", "jian", "ze", "zha", "lian", "lou", "can",
				"ou", "guan", "xi", "zhuo", "ao", "ao", "jin", "zhe", "yi", "hu", "jiang", "man", "chao", "han", "hua", "chan", "xu", "zeng", "se", "xi", "she", "dui", "zheng", "nao", "lan", "e",
				"ying", "jue", "ji", "zun", "jiao", "bo", "hui", "zhuan", "wu", "jian,zen", "zha", "shi,zhi", "qiao", "tan", "zen", "pu", "sheng", "xuan", "zao", "zhan", "dang", "sui", "qian", "ji",
				"jiao", "jing", "lian", "nou", "yi", "ai", "zhan", "pi", "hui", "hua", "yi", "yi", "shan", "rang", "nou", "qian", "zhui", "ta", "hu", "zhou", "hao", "ni", "ying", "jian", "yu",
				"jian", "hui", "du,dou", "zhe", "xuan", "zan", "lei", "shen", "wei", "chan", "li", "yi", "bian", "zhe", "yan", "e", "chou", "wei", "chou", "yao", "chan", "rang", "yin", "lan", "chen",
				"huo", "zhe", "huan", "zan", "yi", "dang", "zhan", "yan", "du", "yan", "ji", "ding", "fu", "ren", "ji", "jie", "hong", "tao", "rang", "shan", "qi", "tuo", "xun", "yi", "xun", "ji",
				"ren", "jiang", "hui", "ou", "ju", "ya", "ne", "xu", "e", "lun", "xiong", "song", "feng", "she", "fang", "jue", "zheng", "gu", "he", "ping", "zu", "shi,zhi", "xiong", "zha", "su",
				"zhen", "di", "zhou", "ci", "qu", "zhao", "bi", "yi", "yi", "kuang", "lei", "shi", "gua", "shi", "jie", "hui", "cheng", "zhu", "shen", "hua", "dan", "gou", "quan", "gui", "xun", "yi",
				"zheng", "gai", "xiang", "cha", "hun", "xu", "zhou", "jie", "wu", "yu", "qiao", "wu", "gao", "you", "hui", "kuang", "shuo,shui,yue", "song", "ei,ai,e^", "qing", "zhu", "zou", "nuo",
				"du,dou", "zhuo", "fei", "ke", "wei", "yu", "shei,shui", "shen", "diao,tiao", "chan", "liang", "zhun", "sui", "tan", "shen", "yi", "mou", "chen", "die", "huang", "jian", "xie", "xue",
				"ye", "wei", "e", "yu", "xuan", "chan", "zi", "an", "yan", "di", "mi,mei", "pian", "xu", "mo", "dang", "su", "xie", "yao", "bang", "shi", "qian", "mi", "jin", "man", "zhe", "jian",
				"miu", "tan", "jian,zen", "qiao", "lan", "pu", "jue", "yan", "qian", "zhan", "chen", "gu,yu", "qian", "hong", "ya", "jue", "hong", "han", "hong", "qi,xi", "xi", "huo,hua", "liao",
				"han", "du", "long", "dou", "jiang", "qi,kai", "chi", "feng,li", "deng", "wan", "bi", "shu", "xian", "feng", "zhi", "zhi", "yan", "yan", "shi", "chu", "hui", "tun", "yi", "tun", "yi",
				"jian", "ba", "hou", "e", "cu", "xiang", "huan", "jian", "ken", "gai", "qu", "fu", "xi", "bin", "hao", "yu", "zhu", "jia", "fen", "xi", "hu", "wen", "huan", "bin", "di", "zong",
				"fen", "yi", "zhi", "bao", "chai", "han", "pi", "na", "pi", "gou", "duo", "you", "diao", "mo", "si", "xiu", "huan", "kun", "he", "he,hao,mo", "mo", "an", "mao", "li", "ni", "bi",
				"yu", "jia", "tuan", "mao", "pi", "xi", "e", "ju", "mo", "chu", "tan", "huan", "qu", "bei", "zhen", "yuan,yun", "fu", "cai", "gong", "te", "yi", "hang", "wan", "pin", "huo", "fan",
				"tan", "guan", "ze,zhai", "zhi", "er", "zhu", "shi", "bi", "zi", "er", "gui", "pian", "bian", "mai", "dai", "sheng", "kuang", "fei", "tie", "yi", "chi", "mao", "he", "bi,ben", "lu",
				"lin,ren", "hui", "gai", "pian", "zi", "jia,gu", "xu", "zei", "jiao", "gai", "zang", "jian", "ying", "xun", "zhen", "she", "bin", "bin", "qiu", "she", "chuan", "zang", "zhou", "lai",
				"zan", "si,ci", "chen", "shang", "tian", "pei", "geng", "xian", "mai", "jian", "sui", "fu", "dan", "cong", "cong", "zhi", "ji", "zhang", "du", "jin", "xiong", "shun", "yun", "bao",
				"zai", "lai", "feng", "cang", "ji", "sheng", "ai", "zhuan,zuan", "fu", "gou", "sai", "ze", "liao", "wei", "bai", "chen", "zhuan", "zhi", "zhui", "biao", "yun", "zeng", "tan", "zan",
				"yan", "none", "shan", "wan", "ying", "jin", "gan", "xian", "zang", "bi", "du", "shu", "yan", "none", "xuan", "long", "gan", "zang", "bei", "zhen", "fu", "yuan,yun", "gong", "cai",
				"ze", "xian", "bai", "zhang", "huo", "zhi", "fan", "tan", "pin", "bian", "gou", "zhu", "guan", "er", "jian", "bi,ben", "shi", "tie", "gui", "kuang", "dai", "mao", "fei", "he", "yi",
				"zei", "zhi", "jia,gu", "hui", "zi", "lin", "lu", "zang", "zi", "gai", "jin", "qiu", "zhen", "lai", "she", "fu", "du", "ji", "shu", "shang", "ci", "bi", "zhou", "geng", "pei", "dan",
				"lai", "feng", "zhui", "fu", "zhuan,zuan", "sai", "ze", "yan", "zan", "yun", "zeng", "shan", "ying", "gan", "chi", "xi", "she", "nan", "xiong", "xi", "cheng", "he", "cheng", "zhe",
				"xia", "tang", "zou", "zou", "li", "jiu", "fu", "zhao", "gan", "qi", "shan", "qiong", "qin", "xian", "ci", "jue", "qin", "chi", "ci", "chen", "chen", "die", "ju,qie", "chao", "di",
				"se", "zhan", "zhu", "yue", "qu", "jie", "chi", "chu", "gua", "xue", "zi", "tiao", "duo", "lie", "gan", "suo", "cu", "xi", "zhao", "su", "yin", "ju", "jian", "que", "tang", "chuo",
				"cui", "lu", "qu,cu", "dang", "qiu", "zi", "ti", "qu,cu", "chi", "huang", "qiao", "qiao", "yao", "zao", "yue", "none", "zan", "zan", "zu,ju", "pa", "bao,bo", "ku", "he", "dun", "jue",
				"fu", "chen", "jian", "fang", "zhi", "ta", "yue", "pa", "qi", "yue", "qiang", "tuo", "tai", "yi", "nian", "ling", "mei", "ba", "die", "ku", "tuo", "jia", "ci", "pao", "qia", "zhu",
				"ju", "die", "zhi", "fu", "pan", "ju", "shan", "bo", "ni", "ju", "li,luo", "gen", "yi", "ji", "dai", "xian", "jiao", "duo", "chu", "quan", "kua", "zhuai,shi", "gui", "qiong", "kui",
				"xiang", "chi", "lu", "beng", "zhi", "jia", "tiao", "cai", "jian", "da", "qiao", "bi", "xian", "duo", "ji", "ju", "ji", "shu", "tu", "chu", "xing", "nie", "xiao", "bo", "xue", "qun",
				"mou", "shu", "liang", "yong", "jiao,jue", "chou", "xiao", "none", "ta", "jian", "qi", "wo", "wei", "chuo", "jie", "ji", "nie", "ju", "ju", "lun", "lu", "leng", "huai", "ju", "chi",
				"wan", "quan", "ti", "bo", "zu", "qie", "qi", "cu", "zong", "cai", "zong", "pan", "zhi", "zheng", "dian,die", "zhi", "yu", "duo", "dun", "chun", "yong", "zhong", "di", "zha", "chen",
				"chuai", "jian", "gua", "tang", "ju", "fu", "zu", "die", "pian", "rou", "nuo", "ti", "cha", "tui", "jian", "dao", "cuo", "xi,qi", "ta", "qiang", "zhan", "dian", "ti", "ji", "nie",
				"pan", "liu", "zhan", "bi", "chong", "lu", "liao", "cu", "tang", "dai", "su", "xi", "kui", "ji", "zhi", "qiang", "di,zhi", "man,pan", "zong", "lian", "beng", "zao", "nian", "bie",
				"tui", "ju", "deng", "ceng", "xian", "fan", "chu", "zhong", "dun,cun", "bo", "cu", "zu", "jue", "jue", "lin", "ta", "qiao", "qiao", "pu", "liao", "dun", "cuan", "kuang", "zao", "ta",
				"bi", "bi", "zhu", "ju", "chu", "qiao", "dun", "chou", "ji", "wu", "yue", "nian", "lin", "lie", "zhi", "li", "zhi", "chan", "chu", "duan", "wei", "long", "lin", "xian", "wei", "zuan",
				"lan", "xie", "rang", "xie", "nie", "ta", "qu", "jie", "cuan", "zuan", "xi", "kui", "jue", "lin", "shen,juan", "gong", "dan", "none", "qu", "ti", "duo", "duo", "gong", "lang", "none",
				"luo", "ai", "ji", "ju", "tang", "none", "none", "yan", "none", "kang", "qu", "lou", "lao", "duo", "zhi", "none", "ti", "dao", "none", "yu", "che,ju", "ya,zha,ga", "gui", "jun",
				"wei", "yue", "xin", "di", "xuan", "fan", "ren", "shan", "qiang", "shu", "tun", "chen", "dai", "e", "na", "qi", "mao", "ruan", "ren", "qian", "zhuan,zhuai", "hong", "hu", "qu",
				"huang", "di", "ling", "dai", "ao", "zhen", "fan", "kuang", "ang", "peng", "bei", "gu", "gu", "pao", "zhu", "rong,fu", "e", "ba", "zhou,zhu", "zhi", "yao", "ke", "yi", "qing", "shi",
				"ping", "er", "qiong", "ju", "jiao", "guang", "lu", "kai", "quan", "zhou", "zai", "zhi", "ju", "liang", "yu", "shao", "you", "huan", "yun", "zhe", "wan", "fu", "qing", "zhou", "ni",
				"ling", "zhe", "zhan", "liang", "zi", "hui", "wang", "chuo", "guo", "kan", "yi", "peng", "qian", "gun", "nian", "ping", "guan", "bei", "lun", "pai", "liang", "ruan", "rou", "ji",
				"yang", "xian", "chuan", "cou", "chun", "ge", "you", "hong", "shu", "fu", "zi", "fu", "wen", "ben", "zhan", "yu", "wen", "tao", "gu", "zhen", "xia", "yuan", "lu", "jiu", "chao",
				"zhuan,zhuai", "wei", "hun", "none", "che,zhe", "jiao", "zhan", "pu", "lao", "fen", "fan", "lin", "ge", "se", "kan", "huan", "yi", "ji", "dui", "er", "yu", "xian", "hong", "lei",
				"pei", "li", "li", "lu", "lin", "che,ju", "ya,zha,ga", "gui", "xuan", "dai", "ren", "zhuan,zhuai", "e", "lun", "ruan", "hong", "gu", "ke", "lu", "zhou", "zhi", "yi", "hu", "zhen",
				"li", "yao", "qing", "shi", "zai", "zhi", "jiao", "zhou", "quan", "lu", "jiao", "zhe", "fu", "liang", "nian", "bei", "hui", "gun", "wang", "liang", "chuo", "zi", "cou", "fu", "ji",
				"wen", "shu", "pei", "yuan", "xia", "zhan", "lu", "zhe", "lin", "xin", "gu", "ci", "ci" };
		
		return result;
	}
}
