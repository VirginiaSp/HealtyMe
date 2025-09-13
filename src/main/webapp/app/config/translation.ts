import { Storage, setTranslationData } from 'app/shared/util/translation-utils';
import { setLocale } from 'app/shared/reducers/locale';

// Load translation data
import globalTranslations from '../../i18n/en/global.json';

// Set default translation data
setTranslationData(globalTranslations);

export const languages: any = {
  en: { name: 'English' },
  // Custom language support can be added here
};

export const locales = Object.keys(languages).sort();

export const registerLocale = store => {
  store.dispatch(setLocale(Storage.session.get('locale', 'en')));
};
