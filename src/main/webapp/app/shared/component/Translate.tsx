/**
 * Custom Translate component to replace react-jhipster Translate
 */
import React, { ReactNode } from 'react';
import { translate } from '../util/translation-utils';

interface TranslateProps {
  contentKey: string;
  interpolate?: Record<string, any>;
  children?: ReactNode;
}

export const Translate: React.FC<TranslateProps> = ({ contentKey, interpolate, children }) => {
  const translatedText = translate(contentKey, interpolate);

  // If translation is not found and children are provided, use children as fallback
  if (translatedText === contentKey && children) {
    return <>{children}</>;
  }

  return <>{translatedText}</>;
};
