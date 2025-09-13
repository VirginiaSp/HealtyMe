import { Storage, setTranslationData } from 'app/shared/util/translation-utils';
import { setLocale } from 'app/shared/reducers/locale';

// Load translation data
import globalTranslations from '../../i18n/en/global.json';
import homeTranslations from '../../i18n/en/home.json';
import loginTranslations from '../../i18n/en/login.json';
import registerTranslations from '../../i18n/en/register.json';
import activateTranslations from '../../i18n/en/activate.json';
import passwordTranslations from '../../i18n/en/password.json';
import resetTranslations from '../../i18n/en/reset.json';
import settingsTranslations from '../../i18n/en/settings.json';
import userProfileTranslations from '../../i18n/en/userProfile.json';
import userManagementTranslations from '../../i18n/en/user-management.json';

// Combine all translations
const allTranslations = {
  ...globalTranslations,
  ...homeTranslations,
  ...loginTranslations,
  ...registerTranslations,
  ...activateTranslations,
  ...passwordTranslations,
  ...resetTranslations,
  ...settingsTranslations,
  ...userProfileTranslations,
  ...userManagementTranslations,
};

// Set default translation data
setTranslationData(allTranslations);

export const languages: any = {
  en: { name: 'English' },
  // Custom language support can be added here
};

export const locales = Object.keys(languages).sort();

export const registerLocale = store => {
  store.dispatch(setLocale(Storage.session.get('locale', 'en')));
};
