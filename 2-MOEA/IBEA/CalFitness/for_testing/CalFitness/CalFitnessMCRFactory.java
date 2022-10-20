/*
 * MATLAB Compiler: 6.6 (R2018a)
 * Date: Fri May 20 19:06:42 2022
 * Arguments: 
 * "-B""macro_default""-W""java:CalFitness,Fitness""-T""link:lib""-d""F:\\科研\\MOEA\\IBEA\\CalFitness\\for_testing""class{Fitness:F:\\科研\\MOEA\\IBEA\\CalFitness.m}"
 */

package CalFitness;

import com.mathworks.toolbox.javabuilder.*;
import com.mathworks.toolbox.javabuilder.internal.*;

/**
 * <i>INTERNAL USE ONLY</i>
 */
public class CalFitnessMCRFactory
{
   
    
    /** Component's uuid */
    private static final String sComponentId = "CalFitness_5FA1E222C54A00FDC1E9D6DE109D6B49";
    
    /** Component name */
    private static final String sComponentName = "CalFitness";
    
   
    /** Pointer to default component options */
    private static final MWComponentOptions sDefaultComponentOptions = 
        new MWComponentOptions(
            MWCtfExtractLocation.EXTRACT_TO_CACHE, 
            new MWCtfClassLoaderSource(CalFitnessMCRFactory.class)
        );
    
    
    private CalFitnessMCRFactory()
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
            CalFitnessMCRFactory.class, 
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
