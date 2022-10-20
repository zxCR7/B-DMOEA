/*
 * MATLAB Compiler: 6.6 (R2018a)
 * Date: Sat May 15 19:10:42 2021
 * Arguments: 
 * "-B""macro_default""-W""java:GetBDAdistance,BDADistance""-T""link:lib""-d""F:\\科研\\MOEA\\B\\1\\GetBDAdistance\\for_testing""class{BDADistance:F:\\科研\\MOEA\\B\\GetBDAdistance.m}"
 */

package GetBDAdistance;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class GetBDAdistanceMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "GetBDAdistan_4A5B8A4EF2E4982BD353E61F91B1C80D";
    
    /** Component name */
    private static final String sComponentName = "GetBDAdistance";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(GetBDAdistanceMCRFactory.class)
        );
    
    
    private GetBDAdistanceMCRFactory()
    {
        // Never called.
    }
    
    public static MWMCR newInstance(MWComponentOptions componentOptions) throws MWException
    {
        if (null == componentOptions.getCtfSource()) {
            componentOptions = new MWComponentOptions(componentOptions);
            componentOptions.setCtfSource(sDefaultComponentOptions.getCtfSource());
        }
        return MWMCR.newInstance(
            componentOptions, 
            GetBDAdistanceMCRFactory.class, 
            sComponentName, 
            sComponentId,
            new int[]{9,4,0}
        );
    }
    
    public static MWMCR newInstance() throws MWException
    {
        return newInstance(sDefaultComponentOptions);
    }
}
