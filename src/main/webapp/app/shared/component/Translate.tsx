/**
 * Custom Translate component to replace react-jhipster Translate
 */
import React, { ReactNode } from 'react';
import { translate } from '../util/translation-utils';

interface TranslateProps {
  contentKey: string;
  interpolate?: Record<string, any>;
  children?: ReactNode;
  component?: string;
}

export const Translate: React.FC<TranslateProps> = ({ contentKey, interpolate, children, component = 'span' }) => {
  const translatedText = translate(contentKey, interpolate);

  // If translation is not found and children are provided, use children as fallback
  const content = translatedText === contentKey && children ? children : translatedText;

  // Create the appropriate element based on the component prop
  return React.createElement(component, {}, content);
};
