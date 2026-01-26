import { createI18n } from 'vue-i18n'
import ko from './locales/ko.json'
import enUS from './locales/en-US.json'
import enGB from './locales/en-GB.json'
import ja from './locales/ja.json'
import zhCN from './locales/zh-CN.json'
import zhTW from './locales/zh-TW.json'

const i18n = createI18n({
    legacy: false, // Use Composition API
    locale: localStorage.getItem('user-locale') || 'ko', // Default locale from storage or ko
    fallbackLocale: 'en-US',
    messages: {
        'ko': ko,
        'en-US': enUS,
        'en-GB': enGB,
        'ja': ja,
        'zh-CN': zhCN,
        'zh-TW': zhTW
    }
})

export default i18n
